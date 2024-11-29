package scalangband.model.level

import scalangband.model.location.Coordinates
import scalangband.model.tile.{Floor, PermanentWall, Tile}

import scala.util.Random

object EmptyLevelGenerator extends LevelGenerator {
  override def generateLevelWithoutStairs(random: Random, depth: Int): Level = {
    val level = LevelGenerator.generateRandomlySizedWallFilledLevel(random, depth)

    for (rowIdx <- 1 until level.height - 1) {
      for (colIdx <- 1 until level.width - 1) {
        val coordinates = Coordinates(rowIdx, colIdx)
        level.setTile(coordinates, Floor.empty(coordinates))
      }
    }

    level
  }
}