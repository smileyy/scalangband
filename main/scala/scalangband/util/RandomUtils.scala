package scalangband.util

import scala.util.Random

object RandomUtils {
  def randomBetween(random: Random, min: Int, max: Int): Int = {
    random.nextInt(max - min) + min
  }
}
