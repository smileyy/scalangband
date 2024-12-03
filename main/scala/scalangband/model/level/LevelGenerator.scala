package scalangband.model.level

import scala.util.Random

trait LevelGenerator {
  def generateLevel(random: Random, depth: Int): Level
}
object LevelGenerator {
  val minWidth: Int = 96
  val maxWidth: Int = 120
  val increments = 4
  
  private def randomLevelDimensions(random: Random): (Int, Int) = {
    val increment = random.nextInt((maxWidth - minWidth) / increments)
    val width = minWidth + (increment * increments)
    val height = (width / 4) * 2

    (width, height)
  }
}