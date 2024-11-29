package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.item.Item
import scalangband.model.location.Coordinates

import scala.collection.mutable

class Floor(coordinates: Coordinates, var occupant: Option[Creature] = None, val items: mutable.Seq[Item]) extends OccupiableTile(coordinates){
  override def setOccupant(occupant: Creature): Unit = this.occupant = Some(occupant)
  override def clearOccupant(): Unit = this.occupant = None

  override def representation: Representable = {
    //    super.representation
    if (occupant.isDefined) occupant.get
    else if (items.nonEmpty) items.head
    else this
  }

}
object Floor {
  def empty(coordinates: Coordinates) = new Floor(coordinates, items = mutable.Seq.empty)
}