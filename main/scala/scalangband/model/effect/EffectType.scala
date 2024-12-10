package scalangband.model.effect

import scalangband.bridge.actionresult.{ActionResult, MessagesResult}
import scalangband.model.util.DiceRoll

sealed trait EffectType {
  def affectedResult: ActionResult
  def affectedMoreResult: ActionResult
  def clearedResult: ActionResult

  override def toString: String = getClass.getSimpleName
}

object Confusion extends EffectType {
  def apply(turns: DiceRoll): EffectFactory = {
    new EffectFactory(this, turns)
  }
  
  override def affectedResult: ActionResult = MessagesResult("You are confused!")
  override def affectedMoreResult: ActionResult = MessagesResult("You are more confused!")
  override def clearedResult: ActionResult = MessagesResult("You are no longer confused.")
}