package scalangband.model

import scalangband.model.action.GameAction
import scalangband.model.action.result.ActionResult
import scalangband.model.fov.FieldOfViewCalculator
import scalangband.model.level.{Level, LevelGenerator, RandomWeightedLevelGenerator}
import scalangband.model.location.Coordinates
import scalangband.model.settings.Settings
import scalangband.model.tile.{DownStairs, OccupiableTile}

import scala.util.Random

class Game(seed: Long, val random: Random, var settings: Settings, val levelGenerator: LevelGenerator, val fov: FieldOfViewCalculator, val player: Player, var playerCoordinates: Coordinates, val town: Level, var level: Level) {
  def playerTile: OccupiableTile = level(playerCoordinates).asInstanceOf[OccupiableTile]
  
  def takeAction(action: GameAction): ActionResult = {
    println(s"Taking action $action")
    val result = action.apply(this)
    fov.recompute(playerCoordinates, level)
    result
  }
}

object Game {
  def newGame(seed: Long, random: Random, settings: Settings, player: Player): Game = {
    val levelGenerator = RandomWeightedLevelGenerator()

    val town: Level = levelGenerator.generateLevel(random, 0)

    val startingTile = town.randomTile(random, classOf[DownStairs])
    startingTile.setOccupant(player)

    val fov = new FieldOfViewCalculator(25)
    fov.recompute(startingTile.coordinates, town)
    new Game(seed, random, settings, levelGenerator, fov, player, startingTile.coordinates, town, town)
  }
}