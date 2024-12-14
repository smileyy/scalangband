package scalangband.model.player

import scalangband.model.player.race.Race

case class Experience(current: Int, max: Int) {
  def +(xp: Int): Experience = {
    Experience(current + xp, max + xp)
  }
}
object Experience {
  def None: Experience = Experience(0, 0)
}

object ExperienceTable {
  def getLevel(race: Race, xp: Int): Int = {
    val effectiveXp = (100 / race.experienceFactor) * xp
    
    effectiveXp match {
      case x if x < 10 => 1
      case x if x < 25 => 2
      case x if x < 45 => 3
      case _ => 4
    }
  }
}