package scalangband.model.tile

import scalangband.model.Creature

class DownStairs(occ: Option[Creature] = None) extends OccupiableTile(occ)
class UpStairs(occ: Option[Creature] = None) extends OccupiableTile(occ)
