package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.location.Coordinates

abstract class Stairs(occupant: Option[Creature]) extends OccupiableTile(occupant)
class DownStairs(occupant: Option[Creature] = None) extends Stairs(occupant)
class UpStairs(occupant: Option[Creature] = None) extends Stairs(occupant)
