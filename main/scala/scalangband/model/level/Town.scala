package scalangband.model.level

import scalangband.model.location.Coordinates
import scalangband.model.tile.{DownStairs, Floor, PermanentWall, Tile}

import scala.util.Random

class TownLevel(tiles: Array[Array[Tile]]) extends Level(tiles, 0)

object TownGenerator extends LevelGenerator {
  private val width = 60
  private val height = 36

  override def generateLevelWithoutStairs(random: Random, depth: Int): Level = {
    val level = LevelGenerator.generateWallFilledLevel(60, 36, depth)

    for (rowIdx <- 1 until level.height - 1) {
      for (colIdx <- 1 until level.width - 1) {
        val coordinates = Coordinates(rowIdx, colIdx)
        val floor = Floor.empty(coordinates)
        level.setTile(coordinates, floor)
      }
    }

    level
  }

  override def numberOfDownStairsForIntermediateLevel(random: Random): Int = 1
  override def numberOfUpStairsForIntermediateLevel(random: Random): Int = 1
}