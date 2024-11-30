package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.item.Item
import scalangband.model.location.Coordinates

import scala.collection.mutable

class Floor(coordinates: Coordinates, occ: Option[Creature], val items: mutable.Seq[Item]) extends OccupiableTile(coordinates, occ){
  override def representation: Representable = {
    if (occupied) occupant.get
    else if (items.nonEmpty) items.head
    else this
  }
}
object Floor {
  def empty(coordinates: Coordinates) = new Floor(coordinates, None, mutable.Seq.empty)
}