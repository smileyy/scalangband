package scalangband.model.player

import scalangband.model.item.Item

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Inventory(var items: ListBuffer[Item]) {
  def addItem(item: Item): Unit = items += item

  override def toString: String = items.map(_.name).mkString("(", ",", ")")
}
object Inventory {
  def empty(): Inventory = new Inventory(ListBuffer.empty)
}

