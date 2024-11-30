package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.location.Coordinates

class ClosedDoor(coordinates: Coordinates) extends Tile(coordinates) {
  override def opaque: Boolean = true
  override def occupant: Option[Creature] = None
}
class OpenDoor(coordinates: Coordinates, occupant: Option[Creature]) extends OccupiableTile(coordinates, occupant)
class BrokenDoor(coordinates: Coordinates, occupant: Option[Creature]) extends OccupiableTile(coordinates, occupant)