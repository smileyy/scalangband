package scalangband.model

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.ActionResult
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.fov.FieldOfViewCalculator
import scalangband.model.item.{Armory, Item}
import scalangband.model.level.*
import scalangband.model.location.Coordinates
import scalangband.model.monster.{Bestiary, Monster}
import scalangband.model.player.action.{GoDownStairsAction, GoUpStairsAction, PlayerAction, PlayerPassAction}
import scalangband.model.player.{Player, PlayerAccessor, PlayerCallback}
import scalangband.model.scheduler.SchedulerQueue
import scalangband.model.settings.Settings
import scalangband.model.tile.{DownStairs, Floor, OccupiableTile, UpStairs}
import scalangband.model.util.RandomUtils.randomElement
import scalangband.model.util.TileUtils.allCoordinatesFor

import scala.annotation.tailrec
import scala.util.Random

class Game(
    seed: Long,
    val random: Random,
    val settings: Settings,
    val armory: Armory,
    val bestiary: Bestiary,
    val player: Player,
    val town: DungeonLevel,
    var level: DungeonLevel,
    var turn: Int,
    var debug: Boolean = false
) {
  val fov = new FieldOfViewCalculator()
  fov.recompute(player.coordinates, town, player.lightRadius)

  val levelGenerator: LevelGenerator = RandomWeightedLevelGenerator()

  var queue: SchedulerQueue = SchedulerQueue(level.creatures)

  val accessor: GameAccessor = new GameAccessor(this)
  val callback: GameCallback = new GameCallback(this)

  def takeAction(playerAction: PlayerAction): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    results = results ::: player.beforeNextAction()
    if (player.isDead) {
      // if they died from, e.g., poison or bleeding, we can exit early
    } else {
      results = playerAction.apply(accessor, callback) ::: results

      playerAction match {
        case GoUpStairsAction =>
          // recreate the queue -- this is okay even if the player didn't successfully go up stairs
          queue = SchedulerQueue(level.creatures)
        case GoDownStairsAction =>
          queue = SchedulerQueue(level.creatures)
        case _ =>
          if (playerAction.energyRequired > 0) {
            val player = queue.poll().asInstanceOf[Player]
            val beforePlayerActionResults = player.beforeNextAction()
            player.deductEnergy(playerAction.energyRequired)
            queue.insert(player)

            val monsterActionResults = takeMonsterActions()
            results = monsterActionResults ::: results
          }
      }
    }

    fov.recompute(player.coordinates, level, player.lightRadius)
    results
  }

  private def takeMonsterActions(): List[ActionResult] = {
    if (queue.peek.energy <= 0) startNextTurn()

    @tailrec
    def loopUntilPlayerIsAtHeadOfQueue(results: List[ActionResult] = List.empty): List[ActionResult] = {
      queue.poll() match {
        case p: Player =>
          queue.push(p)
          results
        case monster: Monster if monster.awake =>
          var monsterActionResults: List[ActionResult] = List.empty

          monsterActionResults = monster.beforeNextAction(accessor, callback) ::: monsterActionResults

          val action = monster.getAction(accessor)
          monsterActionResults = action.apply(monster, accessor, callback) ::: monsterActionResults

          monster.deductEnergy(action.energyRequired)
          queue.insert(monster)

          if (queue.peek.energy <= 0) startNextTurn()

          loopUntilPlayerIsAtHeadOfQueue(monsterActionResults ::: results)
        case monster: Monster =>
          monster.deductEnergy(monster.speed)
          queue.insert(monster)
          if (queue.peek.energy <= 0) startNextTurn()
          loopUntilPlayerIsAtHeadOfQueue(results)
      }
    }

    // this will be in reverse order (most recent action first), but we reverse everything at the end of all the
    // in order to put the player's action result first
    var results = loopUntilPlayerIsAtHeadOfQueue()

    if (player.incapacitated) {
      results = takeAction(PlayerPassAction) ::: results
    }

    results
  }

  def startNextTurn(): Unit = {
    turn = turn + 1
    level.startNextTurn()
    queue = SchedulerQueue(level.creatures)
  }

  //
  // Debugging
  //

  def enableDebug(): Unit = {
    debug = true
  }

  def disableDebug(): Unit = {
    debug = false
  }

  def debugLevel(): Unit = {
    level.debug = true
    fov.recompute(player.coordinates, level, player.lightRadius)
  }
}
object Game {
  private val Logger = LoggerFactory.getLogger(classOf[Game])

  val BaseEnergyUnit: Int = 20
  val MaxDungeonDepth: Int = 100

  def apply(seed: Long, random: Random, settings: Settings, player: Player): Game = {
    val armory: Armory = Armory()
    val bestiary: Bestiary = Bestiary(armory)
    val town: DungeonLevel = Town(random, armory)

    val start = randomElement(random, allCoordinatesFor(town.tiles, tile => tile.isInstanceOf[DownStairs]))
    town.addPlayer(start, player)

    new Game(seed, random, settings, armory, bestiary, player, town, town, 0)
  }
}

class GameAccessor(private val game: Game) {
  // this has to be a `def` since the current level of the game is mutable
  def level: DungeonLevelAccessor = new DungeonLevelAccessor(game.level)
  val player: PlayerAccessor = new PlayerAccessor(game.player)

  def armory: Armory = game.armory
  def playerTile: OccupiableTile = level.tile(player.coordinates).asInstanceOf[OccupiableTile]
}

class GameCallback(private val game: Game) {
  private val logger = LoggerFactory.getLogger(getClass)

  // this has to be a `def` since the current level of the game is mutable
  def level: LevelCallback = new LevelCallback(game.level)
  val player: PlayerCallback = game.player.callback

  def movePlayerTo(targetCoordinates: Coordinates): Unit = {
    game.level.moveOccupant(game.player.coordinates, targetCoordinates)
  }

  def killMonster(monster: Monster): Unit = {
    val coordinates = monster.coordinates
    val tile = game.level(coordinates).asInstanceOf[OccupiableTile]

    tile match {
      case floor: Floor => floor.addItems(monster.inventory.toList)
      // TODO #25: scatter the item nearby if it lands on a non-Floor tile
      case _ => logger.info("Oops, an item disappeared into the aether")
    }

    game.queue.remove(monster)
    game.level(coordinates).asInstanceOf[OccupiableTile].removeOccupant()
  }

  def addItemToTile(coordinates: Coordinates, item: Item): Unit = {
    game.level(coordinates) match {
      case floor: Floor => floor.addItem(item)
      // TODO #25: scatter the item nearby if it lands on a non-Floor tile
      case _ => logger.info("Oops, an item disappeared into the aether")
    }
  }

  def moveDownTo(newDepth: Int): Unit = {
    val newLevel = game.levelGenerator.generateLevel(game.random, newDepth, game.bestiary)
    val startingCoordinates =
      randomElement(game.random, allCoordinatesFor(newLevel.tiles, tile => tile.isInstanceOf[UpStairs]))
    newLevel.addPlayer(startingCoordinates, game.player)
    game.level = newLevel
    player.resetEnergy()
  }

  def moveUpTo(newDepth: Int): Unit = {
    val newLevel = if (newDepth == 0) {
      game.town
    } else {
      game.levelGenerator.generateLevel(game.random, newDepth, game.bestiary)
    }
    val startingCoordinates =
      randomElement(game.random, allCoordinatesFor(newLevel.tiles, tile => tile.isInstanceOf[DownStairs]))
    newLevel.addPlayer(startingCoordinates, game.player)
    game.level = newLevel
    player.resetEnergy()
  }

  def playerPickup(tile: Floor, item: Item): ActionResult = {
    tile.removeItem(item)
    player.pickUp(item)
  }

  def enableDebug(): Unit = {
    game.enableDebug()
  }

  def debugLevel(): Unit = {
    game.debugLevel()
  }
}
