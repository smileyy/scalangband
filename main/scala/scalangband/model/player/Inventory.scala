package scalangband.model.player

import scalangband.model.item.Item

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Inventory(val items: mutable.IndexedBuffer[Item]) {
  def size: Int = items.size
  
  def addItem(item: Item): Unit = items += item
  def getItem(index: Int): Option[Item] = if (index < items.size) Some(items(index)) else None
  def removeItem(index: Int): Option[Item] = if (index < items.size) Some(items.remove(index)) else None
  
  override def toString: String = items.map(_.name).mkString("(", ",", ")")
}
object Inventory {
  def empty(): Inventory = new Inventory(mutable.IndexedBuffer.empty)
}

