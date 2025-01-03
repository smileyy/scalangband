package scalangband.model.monster.action.spell

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.effect.{Effect, Fear}
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

class FearSpell(duration: DiceRoll) extends MonsterSpellAction {
  override def castSpell(monster: Monster, game: GameAccessor, callback: GameCallback): ActionResult = {
    callback.player.tryToAddEffect(new Effect(Fear, 0, duration.roll()))
  }
}