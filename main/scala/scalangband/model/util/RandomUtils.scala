package scalangband.model.util

import scalangband.model.location.Coordinates

import scala.util.Random

object RandomUtils {
  /**
   * Returns a random number between the min (inclusive) and max (exclusive).
   */
  def randomBetween(random: Random, min: Int, max: Int): Int = random.nextInt(max - min) + min

  /**
   * Returns a random element from the array.
   */
  def randomElement[T](random: Random, array: Array[T]): T = array(random.nextInt(array.length))
  
  def randomPairs(random: Random, firstBound: Int, secondBound: Int): LazyList[(Int, Int)] = {
    LazyList.continually((random.nextInt(firstBound), random.nextInt(secondBound)))
  }
}