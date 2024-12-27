package scalangband.model.effect

import scalangband.model.player.PlayerCallback

class Effect(val effectType: EffectType, val strength: Int, var turns: Int) {
  def onNewTurn(callback: PlayerCallback): Unit = {
    effectType.impactPlayer(strength, callback)
  }

  def +(effect: Effect): Unit = {
    turns = turns + effect.turns
  }

  override def toString: String = s"$effectType($turns)"
}