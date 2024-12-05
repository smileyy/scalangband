package scalangband.model.util

import scala.util.Random

case class Weighted[T](item: T, weight: Int)
object Weighted {

  def selectFrom[T](items: Seq[Weighted[T]]): T = select(new Random, items)
  
  def select[T](random: Random, items: Seq[Weighted[T]]): T = {
    val totalWeights = items.map(_.weight).sum
    var selection = random.nextInt(totalWeights)
    var result = items.head.item

    for (Weighted(generator, weight) <- items) {
      if (selection < weight) {
        result = generator
      } else {
        selection = selection - weight
      }
    }

    result
  }
}
