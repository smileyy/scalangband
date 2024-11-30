package scalangband.model

import scalangband.model.action.GameAction
import scalangband.model.action.result.ActionResult
import scalangband.model.fov.FieldOfViewCalculator
import scalangband.model.level.{Level, LevelGenerator, RandomWeightedLevelGenerator, Town}
import scalangband.model.location.Coordinates
import scalangband.model.settings.Settings
import scalangband.model.tile.{DownStairs, OccupiableTile}
import scalangband.model.util.RandomUtils.randomElement
import scalangband.model.util.TileUtils.allCoordinatesFor

import scala.util.Random

class Game(seed: Long, val random: Random, var settings: Settings, val fov: FieldOfViewCalculator, val player: Player, var playerCoordinates: Coordinates, val town: Level, var level: Level) {
  val levelGenerator: LevelGenerator = RandomWeightedLevelGenerator()
  
  def playerTile: OccupiableTile = level(playerCoordinates).asInstanceOf[OccupiableTile]
  
  def takeAction(action: GameAction): Option[ActionResult] = {
    println(s"The player is taking $action")
    val result = action.apply(this)
    fov.recompute(playerCoordinates, level)
    result
  }
}
object Game {
  val BaseEnergyUnit: Int = 20
  val MaxDungeonDepth: Int = 100

  def newGame(seed: Long, random: Random, settings: Settings, player: Player): Game = {
    val town: Level = Town(random)

    val start = randomElement(random, allCoordinatesFor(town.tiles, tile => tile.isInstanceOf[DownStairs]))
    town(start).asInstanceOf[OccupiableTile].setOccupant(player)

    val fov = new FieldOfViewCalculator(20)
    fov.recompute(start, town)
    new Game(seed, random, settings, fov, player, start, town, town)
  }
}