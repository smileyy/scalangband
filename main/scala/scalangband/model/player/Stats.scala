package scalangband.model.player

class Stats(val str: Strength, val intg: Intelligence, val wis: Wisdom, val dex: Dexterity, val con: Constitution) {
  def allStats: Seq[Stat[?]] = Seq(str, intg, wis, dex, con)

  def toHit: Int = allStats.map(_.toHit).sum
  def toDamage: Int = allStats.map(_.toDamage).sum

  def +(b: StatBonuses): Stats = new Stats(str + b.str, intg + b.intg, wis + b.wis, dex + b.dex, con + b.con)
}
object Stats {
  def apply(str: Int, intg: Int, wis: Int, dex: Int, con: Int): Stats = {
    new Stats(new Strength(str), Intelligence(intg), Wisdom(wis), Dexterity(dex), Constitution(con))
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

class Strength(var value: Int) extends Stat[Strength] {
  override def +(bonus: Int): Strength = if (value + bonus >= 40) Strength(40) else Strength(value + bonus)

  override def toHit: Int = value match {
    case 3            => -3
    case 4            => -2
    case x if x <= 6  => -1
    case x if x <= 17 => 0
    case x if x <= 24 => 1
    case x if x < 38  => x - 23
    case _            => 15
  }

  override def toDamage: Int = value match {
    case x if x <= 4  => -2
    case x if x <= 6  => -1
    case x if x <= 15 => 0
    case 16           => 1
    case x if x <= 19 => 2
    case x if x <= 24 => 3
    case 25           => 4
    case x if x <= 27 => 5
    case 28           => 6
    case x if x <= 38 => x - 22
    case 39           => 18
    case _            => 20
  }
}

class Intelligence(var value: Int) extends Stat[Intelligence] {
  override def +(bonus: Int): Intelligence = if (value + bonus >= 40) Intelligence(40) else Intelligence(value + bonus)
}

class Wisdom(var value: Int) extends Stat[Wisdom] {
  override def +(bonus: Int): Wisdom = if (value + bonus >= 40) Wisdom(40) else Wisdom(value + bonus)
}

class Dexterity(var value: Int) extends Stat[Dexterity] {
  override def toHit: Int = value match {
    case 3            => -3
    case x if x <= 5  => -2
    case x if x <= 7  => -1
    case x if x <= 15 => 0
    case 16           => 1
    case 17           => 2
    case x if x <= 22 => 3
    case x if x <= 26 => 4
    case x if x <= 30 => x - 22
    case x if x <= 32 => 8
    case x if x <= 37 => x - 23
    case _            => 15
  }

  override def toArmor: Int = value match {
    case 3            => -4
    case 4            => -3
    case 5            => -2
    case 6            => -1
    case x if x <= 14 => 0
    case x if x <= 17 => 1
    case x if x <= 22 => 2
    case x if x <= 25 => 3
    case x if x <= 30 => x - 22
    case x if x <= 32 => 9
    case x if x <= 37 => x - 23
    case _            => 15
  }

  override def +(bonus: Int): Dexterity = if (value + bonus >= 40) Dexterity(40) else Dexterity(value + bonus)
}

class Constitution(var value: Int) extends Stat[Constitution] {
  override def +(bonus: Int): Constitution = if (value + bonus >= 40) Constitution(40) else Constitution(value + bonus)
}

case class StatBonuses(str: Int, intg: Int, wis: Int, dex: Int, con: Int) {
  def +(b: StatBonuses): StatBonuses = StatBonuses(str + b.str, intg + b.intg, wis + b.wis, dex + b.dex, con + b.con)
}
object StatBonuses {
  val NoBonus: StatBonuses = StatBonuses(0, 0, 0, 0, 0)
}
