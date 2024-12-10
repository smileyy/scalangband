package scalangband.model.player

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.effect.{Effect, EffectType}

import scala.collection.mutable

class Effects(effectsByType: mutable.Map[EffectType, Effect]) {
  def addEffect(effect: Effect): ActionResult = {
    effectsByType.get(effect.effectType) match {
      case Some(existingEffect) =>
        existingEffect + effect
        effect.effectType.affectedMoreResult
      case None =>
        effectsByType.put(effect.effectType, effect)
        effect.effectType.affectedResult
    }
  }

  def hasEffect(effectType: EffectType): Boolean = {
    effectsByType.contains(effectType)
  }

  def onNewTurn(callback: PlayerCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    effectsByType.values.foreach { effect =>
      effect.turns = effect.turns - 1
      if (effect.turns <= 0) {
        effectsByType.remove(effect.effectType)
        results = effect.effectType.clearedResult :: results
      } else {
        results = effect.onNewTurn(callback) :: results
      }
    }

    results
  }

  override def toString: String = effectsByType.values.mkString("[", ",", "]")
}
object Effects {
  def empty(): Effects = new Effects(mutable.Map.empty)
}
