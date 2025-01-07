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
      case x if x < 10      => 1
      case x if x < 25      => 2
      case x if x < 45      => 3
      case x if x < 70      => 4
      case x if x < 100     => 5
      case x if x < 140     => 6
      case x if x < 200     => 7
      case x if x < 280     => 8
      case x if x < 380     => 9
      case x if x < 500     => 10
      case x if x < 650     => 11
      case x if x < 850     => 12
      case x if x < 1100    => 13
      case x if x < 1400    => 14
      case x if x < 1800    => 15
      case x if x < 2300    => 16
      case x if x < 2900    => 17
      case x if x < 3600    => 18
      case x if x < 4400    => 19
      case x if x < 5400    => 20
      case x if x < 6800    => 21
      case x if x < 8400    => 22
      case x if x < 10200   => 23
      case x if x < 12500   => 24
      case x if x < 17500   => 25
      case x if x < 25000   => 26
      case x if x < 35000   => 27
      case x if x < 50000   => 28
      case x if x < 75000   => 29
      case x if x < 100000  => 30
      case x if x < 150000  => 31
      case x if x < 200000  => 32
      case x if x < 275000  => 33
      case x if x < 350000  => 34
      case x if x < 450000  => 35
      case x if x < 550000  => 36
      case x if x < 700000  => 37
      case x if x < 850000  => 38
      case x if x < 1000000 => 39
      case x if x < 1250000 => 40
      case x if x < 1500000 => 41
      case x if x < 1800000 => 42
      case x if x < 2100000 => 43
      case x if x < 2400000 => 44
      case x if x < 2700000 => 45
      case x if x < 3000000 => 46
      case x if x < 3500000 => 47
      case x if x < 4000000 => 48
      case x if x < 4500000 => 49
      case _                => 50
    }
  }
}
