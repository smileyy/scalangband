package scalangband.model.tile

import scalangband.model.Creature

trait Wall extends Tile {
  override def opaque: Boolean = true
  override def occupant: Option[Creature] = None
}

class RemovableWall extends Wall
class PermanentWall extends Wall