package scalangband.model.level

import scalangband.model.level.TownLevel.{TownHeight, TownWidth}
import scalangband.model.location.Coordinates
import scalangband.model.tile.{DownStairs, Floor, PermanentWall, Tile}

import scala.util.Random

class TownLevel(tiles: Array[Array[Tile]]) extends Level(tiles, 0)
object TownLevel {
  private val TownHeight = 36
  private val TownWidth = 60
}

object TownGenerator extends LevelGenerator {
  override def generateLevelWithoutStairs(random: Random, depth: Int): Level = {
    val level = LevelGenerator.generateWallFilledLevel(60, 36, depth)

    for (rowIdx <- 1 until level.height - 1) {
      for (colIdx <- 1 until level.width - 1) {
        val coordinates = Coordinates(rowIdx, colIdx)
        val floor = Floor.empty(coordinates)
        level.replaceTile(coordinates, floor)
      }
    }

    level
  }

  override def numberOfDownStairsForIntermediateLevel(random: Random): Int = 1
  override def numberOfUpStairsForIntermediateLevel(random: Random): Int = 1
}