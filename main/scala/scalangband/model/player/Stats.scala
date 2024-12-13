package scalangband.model.player

case class Stats(str: Stat, intg: Stat, wis: Stat, dex: Stat, con: Stat) {
  def +(b: StatBonus): Stats = Stats(str + b.str, intg + b.intg, wis + b.wis, dex + b.dex, con + b.con)
}
object Stats {
  def apply(str: Int, intg: Int, wis: Int, dex: Int, con: Int): Stats = {
    new Stats(Stat(str), Stat(intg), Stat(wis), Stat(dex), Stat(con))
  }
}

case class Stat(rawValue: Int) {
  def +(bonus: Int): Stat = Stat(rawValue + bonus)

  override def toString: String = {
    if (rawValue <= 18) rawValue.toString else s"18/${(rawValue - 18) * 10}"
  }
}

case class StatBonus(str: Int, intg: Int, wis: Int, dex: Int, con: Int) {
  def +(b: StatBonus): StatBonus = StatBonus(str + b.str, intg + b.intg, wis + b.wis, dex + b.dex, con + b.con)
}
object StatBonus {
  val NoBonus: StatBonus = StatBonus(0, 0, 0, 0, 0)
}