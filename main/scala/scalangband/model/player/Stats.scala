package scalangband.model.player

class Stats(val str: Str, val intg: Intg, val wis: Wis, val dex: Dex, val con: Con) {
  def allStats: Seq[Stat[?]] = Seq(str, intg, wis, dex, con)
  
  def toHit: Int = allStats.map(_.toHit).sum
  def toDamage: Int = allStats.map(_.toDamage).sum
  
  def +(b: StatBonuses): Stats = new Stats(str + b.str, intg + b.intg, wis + b.wis, dex + b.dex, con + b.con)
}
object Stats {
  def apply(str: Int, intg: Int, wis: Int, dex: Int, con: Int): Stats = {
    new Stats(new Str(str), Intg(intg), Wis(wis), Dex(dex), Con(con))
  }
}

trait Stat[T <: Stat[T]] {
  def value: Int
  def +(bonus: Int): T

  override def toString: String = {
    if (value <= 18) value.toString else s"18/${(value - 18) * 10}"
  }
  
  def toHit: Int = 0
  def toDamage: Int = 0
  def toArmor: Int = 0
}

class Str(var value: Int) extends Stat[Str] {
  override def +(bonus: Int): Str = if (value + bonus >= 40) Str(40) else Str(value + bonus)

  override def toHit: Int = value match {
    case 3 => -3
    case 4 => -2
    case x if x <= 6 => -1
    case x if x <= 17 => 0
    case x if x <= 24 => 1
    case x if x < 38 => x - 23
    case _ => 15
  }

  override def toDamage: Int = value match {
    case x if x <= 4 => -2
    case x if x <= 6 => -1
    case x if x <= 15 => 0
    case 16 => 1
    case x if x <= 19 => 2
    case x if x <= 24 => 3
    case 25 => 4
    case x if x <= 27 => 5
    case 28 => 6
    case x if x <= 38 => x - 22
    case 39 => 18
    case _ => 20
    
  }
}

class Intg(var value: Int) extends Stat[Intg] {
  override def +(bonus: Int): Intg = if (value + bonus >= 40) Intg(40) else Intg(value + bonus)
}

class Wis(var value: Int) extends Stat[Wis] {
  override def +(bonus: Int): Wis = if (value + bonus >= 40) Wis(40) else Wis(value + bonus)
}

class Dex(var value: Int) extends Stat[Dex] {
  override def +(bonus: Int): Dex = if (value + bonus >= 40) Dex(40) else Dex(value + bonus)
}

class Con(var value: Int) extends Stat[Con] {
  override def +(bonus: Int): Con = if (value + bonus >= 40) Con(40) else Con(value + bonus)
}

case class StatBonuses(str: Int, intg: Int, wis: Int, dex: Int, con: Int) {
  def +(b: StatBonuses): StatBonuses = StatBonuses(str + b.str, intg + b.intg, wis + b.wis, dex + b.dex, con + b.con)
}
object StatBonuses {
  val NoBonus: StatBonuses = StatBonuses(0, 0, 0, 0, 0)
}