package scalangband.model.monster.attack

import scalangband.model.effect.{Effect, EffectFactory}
import scalangband.model.element.Element
import scalangband.model.util.DiceRoll

abstract class BlowAttack(
    val damage: DiceRoll,
    val maybeElement: Option[Element],
    val effectFactory: Option[EffectFactory]
) extends MeleeAttack {

  override def power: Int = 40
  override def maybeEffect: Option[Effect] = effectFactory.map(factory => factory.createEffect())
}

class BiteAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effectFactory) {

  override def hitDescription: Option[String] = Some("bites you")
}

class ClawAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effectFactory) {

  override def hitDescription: Option[String] = Some("claws you")
}

class CrawlAttack(dmg: DiceRoll, element: Option[Element] = None, effect: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effect) {

  override def hitDescription: Option[String] = Some("crawls on you")
}

class CrushAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effectFactory) {

  override def hitDescription: Option[String] = Some("crushes you")
}

class GazeAttack(dmg: DiceRoll, element: Option[Element] = None, effect: Option[EffectFactory] = None)
  extends BlowAttack(dmg, element, effect) {

  override def hitDescription: Option[String] = Some("gazes at you")
  override def missDescription: Option[String] = None
}

class PlainAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effectFactory) {}

class SporeAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effectFactory) {

  override def hitDescription: Option[String] = Some("releases spores at you")
  override def missDescription: Option[String] = None
}

class StingAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effectFactory) {

  override def hitDescription: Option[String] = Some("stings you")
}

class TouchAttack(dmg: DiceRoll, element: Option[Element] = None, effectFactory: Option[EffectFactory] = None)
    extends BlowAttack(dmg, element, effectFactory) {

  override def hitDescription: Option[String] = Some("touches you")
}
