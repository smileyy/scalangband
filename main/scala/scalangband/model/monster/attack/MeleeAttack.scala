package scalangband.model.monster.attack

import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.effect.{Effect, EffectFactory}
import scalangband.model.element.Element
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

import scala.util.Random

trait MeleeAttack {
  def attack(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    val toHit = Math.max(monster.level, 1) * 3 + power

    if (Random.nextInt(toHit) > game.player.armorClass) {
      results = hitMessage(monster).map(msg => MessageResult(msg)).getOrElse(NoResult) :: results
      results = callback.player.takeDamage(damage.roll(), maybeElement, maybeEffect) ::: results
    } else {
      results = missMessage(monster).map(msg => MessageResult(msg)).getOrElse(NoResult) :: results
    }

    results
  }

  /**
   * Oddly named to-hit modifier for the attack
   */
  def power: Int

  /**
   * The damage done by the attack
   */
  def damage: DiceRoll
  def maybeElement: Option[Element]
  def maybeEffect: Option[Effect]

  def hitMessage(monster: Monster): Option[String] = Some(s"The ${monster.displayName} hits you.")
  def missMessage(monster: Monster): Option[String] = Some(s"The ${monster.displayName} misses you.")

  override def toString: String = s"${getClass.getSimpleName}($damage)}"
}
