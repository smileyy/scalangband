package scalangband.model.effect

import scalangband.model.util.DiceRoll

class EffectFactory(effectType: EffectType, turns: DiceRoll) {
  def createEffect(): Effect = {
    new Effect(effectType, turns.roll())
  }
}
