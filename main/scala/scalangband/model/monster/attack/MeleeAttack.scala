package scalangband.model.monster.attack

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.effect.Effect
import scalangband.model.element.Element
import scalangband.model.monster.Monster
import scalangband.model.player.PlayerAccessor
import scalangband.model.util.DiceRoll
import scalangband.model.{GameAccessor, GameCallback}

import scala.util.Random

trait MeleeAttack {
  def attack(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    val toHit = Math.max(monster.level, 1) * 3 + power

    if (Random.nextInt(toHit) > game.player.armorClass) {
      val damageDone = damage.roll()
      MeleeAttack.Logger.info(s"${monster.name} hit player for $damageDone")
      results = hitMessage(monster, game.player).map(msg => MessageResult(msg)).getOrElse(NoResult) :: results
      results = callback.player.takeDamage(damageDone, maybeElement, maybeEffect) ::: results
    } else {
      results = missMessage(monster, game.player).map(msg => MessageResult(msg)).getOrElse(NoResult) :: results
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

  def monsterDescription(monster: Monster, player: PlayerAccessor): String = {
    if (!monster.invisible || player.canSeeInvisible) s"The ${monster.displayName}" else "It"
  }
  def hitDescription: Option[String] = Some("hits you")
  def missDescription: Option[String] = Some("misses you")
  
  def hitMessage(monster: Monster, player: PlayerAccessor): Option[String] = {
    hitDescription.map(desc => s"${monsterDescription(monster, player)} $desc.")
  }

  def missMessage(monster: Monster, player: PlayerAccessor): Option[String] = {
    missDescription.map(desc => s"${monsterDescription(monster, player)} $desc.")
  }

  override def toString: String = s"${getClass.getSimpleName}($damage)}"
}
object MeleeAttack {
  private val Logger = LoggerFactory.getLogger(classOf[MeleeAttack])
}
