package scalangband.model.tile

import scalangband.model.Creature

class DownStairs(occupant: Option[Creature] = None) extends OccupiableTile(occupant)
class UpStairs(occupant: Option[Creature] = None) extends OccupiableTile(occupant)
