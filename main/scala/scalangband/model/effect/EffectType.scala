package scalangband.model.effect

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.element.Element
import scalangband.model.player.PlayerCallback
import scalangband.model.util.DiceRoll

sealed trait EffectType {
  def affectedResult: ActionResult
  def affectedMoreResult: ActionResult
  def clearedResult: ActionResult

  def impactPlayer(strength: Int, callback: PlayerCallback): Unit = {}
  
  override def toString: String = getClass.getSimpleName
}

object Confusion extends EffectType {
  def apply(turns: DiceRoll): EffectFactory = {
    new EffectFactory(this, turns)
  }
  
  override def affectedResult: ActionResult = MessageResult("You are confused!")
  override def affectedMoreResult: ActionResult = MessageResult("You are more confused!")
  override def clearedResult: ActionResult = MessageResult("You are no longer confused.")
}

object Paralysis extends EffectType {
  def apply(turns: DiceRoll): EffectFactory = {
    new EffectFactory(this, turns)
  }

  override def affectedResult: ActionResult = MessageResult("You are paralyzed!")
  override def affectedMoreResult: ActionResult = MessageResult("You are paralyzed!")
  override def clearedResult: ActionResult = MessageResult("You can move again.")
}

object Poisoning extends EffectType {
  def apply(turns: DiceRoll, strength: Int = 1): EffectFactory = {
    new EffectFactory(this, turns, strength)
  }

  override def affectedResult: ActionResult = MessageResult("You are poisoned!")
  override def affectedMoreResult: ActionResult = MessageResult("You are more poisoned!")
  override def clearedResult: ActionResult = MessageResult("You are no longer poisoned.")

  override def impactPlayer(strength: Int, callback: PlayerCallback): Unit = {
    callback.takeDamage(strength)
  }
}