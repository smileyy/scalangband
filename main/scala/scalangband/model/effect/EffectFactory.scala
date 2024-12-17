package scalangband.model.effect

import scalangband.model.util.DiceRoll

class EffectFactory(effectType: EffectType, turns: DiceRoll, strength: Int = 0) {
  def createEffect(): Effect = {
    new Effect(effectType, strength, turns.roll())
  }
}
