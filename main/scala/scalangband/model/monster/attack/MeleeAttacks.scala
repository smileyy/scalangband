package scalangband.model.monster.attack

import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

class BlowAttack(val damage: DiceRoll) extends MeleeAttack {
  override def power: Int = 40
}

class CrawlAttack(dmg: DiceRoll) extends BlowAttack(dmg) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} crawls on you.")
  }
}

class SporeAttack(dmg: DiceRoll) extends BlowAttack(dmg) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} releases spores at you.")
  }

  override def missMessage(monster: Monster): Option[String] = None
}

class StingAttack(dmg: DiceRoll) extends BlowAttack(dmg) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} stings you.")
  }
}