package scalangband.model.level

import scalangband.model.monster.Bestiary

import scala.util.Random

trait LevelGenerator {
  def generateLevel(random: Random, depth: Int, bestiary: Bestiary): Level
}
object LevelGenerator {
  val minWidth: Int = 120
  val maxWidth: Int = 160
  val increments = 8
  
  private def randomLevelDimensions(random: Random): (Int, Int) = {
    val increment = random.nextInt((maxWidth - minWidth) / increments)
    val width = minWidth + (increment * increments)
    val height = width / 8

    (width, height)
  }
}