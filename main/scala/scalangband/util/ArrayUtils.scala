package scalangband.util

import scala.util.Random

object ArrayUtils {
  def randomElement[T](random: Random, array: Array[T]): T = array(random.nextInt(array.length))
}
