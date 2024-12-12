package scalangband.model.util

import scala.annotation.tailrec
import scala.util.Random

case class Weighted[T](weight: Int, item: T)
object Weighted {

  def selectFrom[T](items: Seq[Weighted[T]]): T = selectFrom(new Random(), items)
  
  def selectFrom[T](random: Random, items: Seq[Weighted[T]]): T = {
    val totalWeights = items.map(_.weight).sum
    var selection = random.nextInt(totalWeights)

    @tailrec
    def select(items: Seq[Weighted[T]], selection: Int): T = items match {
      case x :: xs if selection < x.weight => x.item
      case x :: xs => select(items.tail, selection - x.weight)
    }

    select(items, selection)
  }
}
