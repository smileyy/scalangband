package scalangband.model.util

import scala.util.Random

case class Weighted[T](weight: Int, item: T)
object Weighted {

  def selectFrom[T](items: Seq[Weighted[T]]): T = select(new Random, items)
  
  def select[T](items: Seq[Weighted[T]]): T = select(new Random(), items)
  
  def select[T](random: Random, items: Seq[Weighted[T]]): T = {
    val totalWeights = items.map(_.weight).sum
    var selection = random.nextInt(totalWeights)
    var result = items.head.item

    for (Weighted(weight, item) <- items) {
      if (selection < weight) {
        result = item
      } else {
        selection = selection - weight
      }
    }

    result
  }
}
