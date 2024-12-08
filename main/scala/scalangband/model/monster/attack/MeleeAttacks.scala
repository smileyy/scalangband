package scalangband.model.monster.attack

import scalangband.model.effect.{Effect, EffectFactory}
import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

abstract class BlowAttack(val damage: DiceRoll, val effectFactory: Option[EffectFactory] = None) extends MeleeAttack {
  override def power: Int = 40

  override def effect: Option[Effect] = effectFactory.map(factory => factory.createEffect())
}

class CrawlAttack(dmg: DiceRoll, effectFactory: Option[EffectFactory] = None) extends BlowAttack(dmg, effectFactory) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} crawls on you.")
  }
}

class SporeAttack(dmg: DiceRoll, effectFactory: Option[EffectFactory] = None) extends BlowAttack(dmg, effectFactory) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} releases spores at you.")
  }

  override def missMessage(monster: Monster): Option[String] = None
}

class StingAttack(dmg: DiceRoll, effectFactory: Option[EffectFactory] = None) extends BlowAttack(dmg, effectFactory) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} stings you.")
  }
}