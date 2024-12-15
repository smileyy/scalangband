package scalangband.model.player

import scalangband.model.item.Item

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Inventory(val items: mutable.IndexedBuffer[Item]) {
  def size: Int = items.size

  def addItem(item: Item): Unit = items += item
  def getItem(idx: Int): Item = items(idx)
  def removeItem(item: Item): Unit = items -= item
  
  override def toString: String = items.map(_.name).mkString("(", ",", ")")
}
object Inventory {
  def empty(): Inventory = new Inventory(mutable.IndexedBuffer.empty)
}

