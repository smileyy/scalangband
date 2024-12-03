package scalangband.model.tile

import scalangband.model.item.Item
import scalangband.model.{Creature, Representable}

import scala.collection.mutable

class Floor(occ: Option[Creature], val items: mutable.Seq[Item]) extends OccupiableTile(occ) {
  override def representation: Representable = {
    if (occupied) occupant.get
    else if (items.nonEmpty) items.head
    else this
  }
}
object Floor {
  def empty() = new Floor(None, mutable.Seq.empty)
}