package scalangband.model.monster.action.spell

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll

class HealSpell(amount: DiceRoll) extends MonsterSpellAction {
  override def castSpell(monster: Monster, game: GameAccessor, callback: GameCallback): ActionResult = {
    monster.heal(amount.roll())

    if (monster.health == monster.maxHealth) {
      MessageResult(s"The ${monster.displayName} looks REALLY healthy!")
    } else {
      MessageResult(s"The ${monster.displayName} looks healthier.")
    }
  }
}
