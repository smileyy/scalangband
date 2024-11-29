package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.location.Coordinates

abstract class Stairs(coordinates: Coordinates, var occupant: Option[Creature]) extends OccupiableTile(coordinates) {
  override def setOccupant(occupant: Creature): Unit = this.occupant = Some(occupant)
  override def clearOccupant(): Unit = this.occupant = None
}
class DownStairs(coordinates: Coordinates, occupant: Option[Creature] = None) extends Stairs(coordinates, occupant)
class UpStairs(coordinates: Coordinates, occupant: Option[Creature] = None) extends Stairs(coordinates, occupant)
