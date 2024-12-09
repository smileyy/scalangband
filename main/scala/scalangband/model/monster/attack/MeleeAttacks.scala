package scalangband.model.monster.attack

import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

class BasicAttack(val damage: DiceRoll) extends MeleeAttack {
  override def power: Int = 40
}

class CrawlAttack(dmg: DiceRoll) extends BasicAttack(dmg) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} crawls on you.")
  }
}

class StingAttack(dmg: DiceRoll) extends BasicAttack(dmg) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} stings you.")
  }
}