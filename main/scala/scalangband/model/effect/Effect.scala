package scalangband.model.effect

import scalangband.bridge.actionresult.{ActionResult, NoResult}
import scalangband.model.player.PlayerCallback

class Effect(val effectType: EffectType, var turns: Int) {
  def onNewTurn(callback: PlayerCallback): ActionResult = NoResult

  def +(effect: Effect): Unit = {
    turns = turns + effect.turns
  }

  override def toString: String = s"$effectType($turns)"
}