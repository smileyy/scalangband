package scalangband.model

import scalangband.model.action.GameAction
import scalangband.model.action.result.ActionResult
import scalangband.model.fov.FieldOfViewCalculator
import scalangband.model.level.{Level, LevelGenerator, RandomWeightedLevelGenerator, Town}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.scheduler.SchedulerQueue
import scalangband.model.settings.Settings
import scalangband.model.tile.{DownStairs, OccupiableTile}
import scalangband.model.util.RandomUtils.randomElement
import scalangband.model.util.TileUtils.allCoordinatesFor

import scala.util.Random

class Game(seed: Long, val random: Random, val settings: Settings, val player: Player, var playerCoordinates: Coordinates, val town: Level, var level: Level, var turn: Int) {
  private val fov = new FieldOfViewCalculator(20)
  fov.recompute(playerCoordinates, town)

  val levelGenerator: LevelGenerator = RandomWeightedLevelGenerator()

  var queue: SchedulerQueue = createSchedulerQueue(level)
  
  def takeTurn(action: GameAction): Seq[ActionResult] = {
    // We know(?) that the player is at the head of the queue
    println(s"The player is taking $action")

    val playerActionResult: Option[ActionResult] = action.apply(this)
    var results: List[Option[ActionResult]] = List(playerActionResult)
    
    if (action.energyRequired != 0) {
      val player = queue.poll()
      player.deductEnergy(action.energyRequired)
      queue.insert(player)
      
      results = takeMonsterActions() ::: results
    }
    
    fov.recompute(playerCoordinates, level)

    results.flatten.reverse
  }

  def takeMonsterActions(): List[Option[ActionResult]] = {
    if (queue.peek.energy <= 0) startNextTurn()
    
    var results = List.empty[Option[ActionResult]]
    while (queue.peek.isInstanceOf[Monster]) {
      val monster = queue.poll().asInstanceOf[Monster]
      val action: GameAction = monster.getAction(level)
      val result: Option[ActionResult] = action.apply(this)
      monster.deductEnergy(action.energyRequired)
      queue.insert(monster)

      results = result :: results

      if (queue.peek.energy <= 0) startNextTurn()
    }
    results
  }
  
  def startNextTurn(): Unit = {
    turn = turn + 1
    level.startNextTurn()
    
    queue = createSchedulerQueue(level)
  }
  
  def createSchedulerQueue(level: Level): SchedulerQueue = {
    val queue = SchedulerQueue.empty()
    level.creatures.foreach(creature => queue.insert(creature))
    println(queue)
    queue
  }

  def playerTile: OccupiableTile = level(playerCoordinates).asInstanceOf[OccupiableTile]
}
object Game {
  val BaseEnergyUnit: Int = 20
  val MaxDungeonDepth: Int = 100

  def newGame(seed: Long, random: Random, settings: Settings, player: Player): Game = {
    val town: Level = Town(random)

    val start = randomElement(random, allCoordinatesFor(town.tiles, tile => tile.isInstanceOf[DownStairs]))
    town.addCreature(start, player)

    new Game(seed, random, settings, player, start, town, town, 0)
  }
}