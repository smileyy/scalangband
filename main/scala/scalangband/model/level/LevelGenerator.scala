package scalangband.model.level

import scalangband.model.location.Coordinates
import scalangband.model.tile.{DownStairs, Floor, RemovableWall, PermanentWall, Tile, UpStairs}

import scala.util.Random

trait LevelGenerator {
  final def generateLevel(random: Random, depth: Int): Level = {
    val level = if (depth == 0) {
      TownGenerator.generateLevelWithoutStairs(random, depth)
    } else {
      generateLevelWithoutStairs(random, depth)
    }

    addStairs(level, random)
    level
  }

  def generateLevelWithoutStairs(random: Random, depth: Int): Level

  private def numberOfDownStairs(random: Random, depth: Int): Int = depth match {
    case 0 => 1
    case 100 => 0
    case x => numberOfDownStairsForIntermediateLevel(random)
  }

  def numberOfDownStairsForIntermediateLevel(random: Random): Int = {
    random.nextInt(3) + 1
  }

  private def numberOfUpStairs(random: Random, depth: Int): Int = depth match {
    case 0 => 0
    case x => numberOfUpStairsForIntermediateLevel(random)
  }

  def numberOfUpStairsForIntermediateLevel(random: Random): Int = {
    random.nextInt(1) + 1
  }

  def addStairs(level: Level, random: Random): Unit = {
    for (i <- 0 until numberOfDownStairs(random, level.depth)) {
      val tile = level.randomTile(random, classOf[Floor])
      level.replaceWithStairs(tile, DownStairs(tile.coordinates))
    }

    for (i <- 0 until numberOfUpStairs(random, level.depth)) {
      val tile = level.randomTile(random, classOf[Floor])
      level.replaceWithStairs(tile, UpStairs(tile.coordinates))
    }
  }
}
object LevelGenerator {
  val minWidth: Int = 96
  val maxWidth: Int = 120
  val increments = 4

  def generateWallFilledLevel(width: Int, height: Int, depth: Int): Level = {
    val tiles = Array.ofDim[Tile](height, width)

    for (rowIdx <- 0 until height) {
      for (colIdx <- 0 until width) {
        tiles(rowIdx)(colIdx) = {
          val coordinates = Coordinates(rowIdx, colIdx)
          if (rowIdx == 0 || colIdx == 0 || rowIdx == height - 1 || colIdx == width - 1) PermanentWall(coordinates)
          else new RemovableWall(coordinates)
        }
      }
    }

    new Level(tiles, depth)
  }

  /**
   * Creates a randomly sized empty level surrounded by {@link PermanentWall}s.
   */
  def generateRandomlySizedWallFilledLevel(random: Random, depth: Int): Level = {
    val (width, height) = randomLevelDimensions(random)
    generateWallFilledLevel(width, height, depth)
  }

  private def randomLevelDimensions(random: Random): (Int, Int) = {
    val increment = random.nextInt((maxWidth - minWidth) / increments)
    val width = minWidth + (increment * increments)
    val height = (width / 4) * 2

    (width, height)
  }
}