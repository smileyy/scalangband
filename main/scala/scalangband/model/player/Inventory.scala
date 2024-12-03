package scalangband.model.player

import scalangband.model.item.Item

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Inventory(var items: ListBuffer[Item]) {
  def addItem(item: Item): Unit = items += item
}
object Inventory {
  def empty(): Inventory = new Inventory(ListBuffer.empty)
}

