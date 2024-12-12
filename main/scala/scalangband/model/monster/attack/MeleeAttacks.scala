package scalangband.model.monster.attack

import scalangband.model.element.Element
import scalangband.model.effect.{Effect, EffectFactory}
import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

abstract class BlowAttack(val damage: DiceRoll, val element: Option[Element], val effectFactory: Option[EffectFactory] = None) extends MeleeAttack {
  override def power: Int = 40

  override def effect: Option[Effect] = effectFactory.map(factory => factory.createEffect())
}

class CrawlAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None) extends BlowAttack(dmg, element, effectFactory) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} crawls on you.")
  }
}

class SporeAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None) extends BlowAttack(dmg, element, effectFactory) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} releases spores at you.")
  }

  override def missMessage(monster: Monster): Option[String] = None
}

class StingAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None) extends BlowAttack(dmg, element, effectFactory) {
  override def hitMessage(monster: Monster): Option[String] = {
    Some(s"The ${monster.displayName} stings you.")
  }
}