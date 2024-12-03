package scalangband.model.tile

import scalangband.model.item.Item
import scalangband.model.{Creature, Representable}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Floor(occ: Option[Creature], val items: mutable.ListBuffer[Item]) extends OccupiableTile(occ) {
  def addItem(item: Item): Unit = {
    items += item
  }
  
  def removeItem(item: Item): Unit = {
    items -= item
  }
  
  override def representation: Representable = {
    if (occupied) occupant.get
    else if (items.size == 1) items.head
    else if (items.size > 1) PileOfItems
    else this
  }
}
object Floor {
  def empty() = new Floor(None, ListBuffer.empty[Item])
}

object PileOfItems extends Representable