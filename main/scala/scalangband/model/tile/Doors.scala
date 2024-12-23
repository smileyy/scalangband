package scalangband.model.tile

import scalangband.model.Creature

class ClosedDoor extends Tile {
  override def opaque: Boolean = true
  override def occupant: Option[Creature] = None
  override def passable: Boolean = false
}
class OpenDoor(occupant: Option[Creature] = None) extends OccupiableTile(occupant)
class BrokenDoor(occupant: Option[Creature] = None) extends OccupiableTile(occupant)