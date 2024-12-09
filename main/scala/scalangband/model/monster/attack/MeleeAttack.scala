package scalangband.model.monster.attack

import scalangband.bridge.actionresult.{ActionResult, MessagesResult, NoResult}
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

import scala.util.Random

trait MeleeAttack {
  def attack(monster: Monster, game: GameAccessor, callback: GameCallback): ActionResult = {
    val toHit = Math.max(monster.level, 1) * 3 + power

    if (Random.nextInt(toHit) > game.player.armorClass) {
      callback.player.takeHit(damage.roll())
      println(s"$this hits")
      hitMessage(monster).map(msg => MessagesResult(List(msg))).getOrElse(NoResult)
    } else {
      println(s"$this misses")
      missMessage(monster).map(msg => MessagesResult(List(msg))).getOrElse(NoResult)
    }
  }

  /**
   * Oddly named to-hit modifier for the attack
   */
  def power: Int

  /**
   * The damage done by the attack
   */
  def damage: DiceRoll

  def hitMessage(monster: Monster): Option[String] = Some(s"The ${monster.displayName} hits you.")
  def missMessage(monster: Monster): Option[String] = Some(s"The ${monster.displayName} misses you.")

  override def toString: String = s"${getClass.getSimpleName}($damage)}"
}
