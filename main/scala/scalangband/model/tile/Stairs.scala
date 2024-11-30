package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.location.Coordinates

abstract class Stairs(coordinates: Coordinates, occupant: Option[Creature]) extends OccupiableTile(coordinates, occupant)
class DownStairs(coordinates: Coordinates, occupant: Option[Creature] = None) extends Stairs(coordinates, occupant)
class UpStairs(coordinates: Coordinates, occupant: Option[Creature] = None) extends Stairs(coordinates, occupant)
