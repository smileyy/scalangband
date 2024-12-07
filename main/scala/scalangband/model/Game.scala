package scalangband.model

import org.slf4j.LoggerFactory
import scalangband.model.action.player.{GoDownStairsAction, GoUpStairsAction, PlayerAction}
import scalangband.model.action.result.ActionResult
import scalangband.model.fov.FieldOfViewCalculator
import scalangband.model.item.Item
import scalangband.model.level.*
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.player.{Player, PlayerAccessor, PlayerCallback}
import scalangband.model.scheduler.SchedulerQueue
import scalangband.model.settings.Settings
import scalangband.model.tile.{DownStairs, Floor, OccupiableTile, UpStairs}
import scalangband.model.util.RandomUtils.randomElement
import scalangband.model.util.TileUtils.allCoordinatesFor

import scala.annotation.tailrec
import scala.util.Random

class Game(seed: Long, val random: Random, val settings: Settings, val player: Player, val town: Level, var level: Level, var turn: Int) {
  private val logger = LoggerFactory.getLogger(this.getClass)

  private val fov = new FieldOfViewCalculator()
  fov.recompute(player.coordinates, town, player.light)

  val levelGenerator: LevelGenerator = RandomWeightedLevelGenerator()

  var queue: SchedulerQueue = SchedulerQueue(level.creatures)

  val accessor: GameAccessor = new GameAccessor(this)
  val callback: GameCallback = new GameCallback(this)

  def takeTurn(playerAction: PlayerAction): Seq[ActionResult] = {
    // We know(?) that the player is at the head of the queue
    logger.info(s"Player is taking $playerAction")

    val playerActionResult: Option[ActionResult] = playerAction.apply(accessor, callback)

    val results: List[Option[ActionResult]] = (playerAction, playerActionResult) match {
      // We don't need / want the monster action loop if the player has successfully gone up or down stairs
      case (GoUpStairsAction, Some(result)) if result.success =>
        queue = SchedulerQueue(level.creatures)
        List(playerActionResult)
      case (GoDownStairsAction, Some(result)) if result.success =>
        queue = SchedulerQueue(level.creatures)
        List(playerActionResult)
      case _ =>
        if (playerAction.energyRequired > 0) {
          val player = queue.poll()
          player.deductEnergy(playerAction.energyRequired)
          queue.insert(player)

          (takeMonsterActions() ::: List(playerActionResult)).reverse
        } else {
          List(playerActionResult)
        }
    }

    fov.recompute(player.coordinates, level, player.light)
    results.flatten.reverse
  }

  private def takeMonsterActions(): List[Option[ActionResult]] = {
    if (queue.peek.energy <= 0) startNextTurn()

    @tailrec
    def loopUntilPlayerIsAtHeadOfQueue(results: List[Option[ActionResult]]): List[Option[ActionResult]] = {
      queue.poll() match {
        case p: Player =>
          queue.push(p)
          results
        case monster: Monster =>
          val action = monster.getAction(accessor)
          logger.info(s"${monster.name} is taking action $action")
          val result: Option[ActionResult] = action.apply(monster, accessor, callback)
          monster.deductEnergy(action.energyRequired)
          queue.insert(monster)

          if (queue.peek.energy <= 0) startNextTurn()

          loopUntilPlayerIsAtHeadOfQueue(result :: results)
      }
    }

    // this will be in reverse order (most recent action first), but we reverse everything at the end of all the
    // in order to put the player's action result first
    loopUntilPlayerIsAtHeadOfQueue(List.empty[Option[ActionResult]])
  }
  
  def startNextTurn(): Unit = {
    turn = turn + 1
    level.startNextTurn()
    
    queue = SchedulerQueue(level.creatures)
  }
}
object Game {
  val BaseEnergyUnit: Int = 20
  val MaxDungeonDepth: Int = 100

  def newGame(seed: Long, random: Random, settings: Settings, player: Player): Game = {
    val town: Level = Town(random)

    val start = randomElement(random, allCoordinatesFor(town.tiles, tile => tile.isInstanceOf[DownStairs]))
    town.addPlayer(start, player)

    new Game(seed, random, settings, player, town, town, 0)
  }
}

class GameAccessor(private val game: Game) {
  // this has to be a `def` since the current level of the game is mutable
  def level: LevelAccessor = new LevelAccessor(game.level)
  val player: PlayerAccessor = new PlayerAccessor(game.player)

  def playerTile: OccupiableTile = level.tile(player.coordinates).asInstanceOf[OccupiableTile]
}

class GameCallback(private val game: Game) {
  private val logger = LoggerFactory.getLogger(getClass)
  
  // this has to be a `def` since the current level of the game is mutable
  def level: LevelCallback = new LevelCallback(game.level)
  val player: PlayerCallback = new PlayerCallback(game.player)

  def movePlayerTo(targetCoordinates: Coordinates): Unit = {
    game.level.moveOccupant(game.player.coordinates, targetCoordinates)
  }

  def killMonster(monster: Monster): Unit = {
    val coordinates = monster.coordinates
    val tile = game.level(coordinates).asInstanceOf[OccupiableTile]
    
    tile match {
      case floor: Floor => floor.addItems(monster.inventory.toList)
      // TODO: scatter the item nearby if it lands on the stairs
      case _ => logger.info("Oops, an item disappeared into the aether")
    }

    game.queue.remove(monster)
    game.level(coordinates).asInstanceOf[OccupiableTile].clearOccupant()
  }

  def moveDownTo(depth: Int): Unit = {
    val newLevel = game.levelGenerator.generateLevel(game.random, game.level.depth + 1)
    val startingCoordinates =
      randomElement(game.random, allCoordinatesFor(newLevel.tiles, tile => tile.isInstanceOf[UpStairs]))
    newLevel.addPlayer(startingCoordinates, game.player)
    game.level = newLevel
    player.resetEnergy()
  }

  def moveUpTo(depth: Int): Unit = {
    val newLevel = if (game.level.depth == 1) {
      game.town
    } else {
      game.levelGenerator.generateLevel(game.random, game.level.depth)
    }
    val startingCoordinates =
      randomElement(game.random, allCoordinatesFor(newLevel.tiles, tile => tile.isInstanceOf[DownStairs]))
    newLevel.addPlayer(startingCoordinates, game.player)
    game.level = newLevel
    player.resetEnergy()
  }
  
  def playerPickup(tile: Floor, item: Item): Unit = { 
    game.player.inventory.addItem(item)
    tile.removeItem(item)
  }
}