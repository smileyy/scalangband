package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.location.Coordinates

class ClosedDoor(coordinates: Coordinates) extends Tile(coordinates) {
  override def opaque: Boolean = true
}
class OpenDoor(coordinates: Coordinates, var occupant: Option[Creature]) extends OccupiableTile(coordinates) {
  override def setOccupant(occupant: Creature): Unit = this.occupant = Some(occupant)
  override def clearOccupant(): Unit = occupant = None
}
class BrokenDoor(coordinates: Coordinates, var occupant: Option[Creature]) extends OccupiableTile(coordinates) {
  override def setOccupant(occupant: Creature): Unit = this.occupant = Some(occupant)
  override def clearOccupant(): Unit = occupant = None
}