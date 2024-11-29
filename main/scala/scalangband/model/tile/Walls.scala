package scalangband.model.tile

import scalangband.model.Representable
import scalangband.model.location.Coordinates

abstract class Wall(coordinates: Coordinates) extends Tile(coordinates), Representable {
  override def opaque: Boolean = true
  override def representation: Representable = this
}

class RemovableWall(coordinates: Coordinates) extends Wall(coordinates)
class PermanentWall(coordinates: Coordinates) extends Wall(coordinates)