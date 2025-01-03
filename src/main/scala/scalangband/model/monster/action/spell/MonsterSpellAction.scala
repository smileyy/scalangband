package scalangband.model.monster.action.spell

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.monster.Monster
import scalangband.model.monster.action.MonsterAction

trait MonsterSpellAction extends MonsterAction {
  override def apply(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = {
    var results: List[ActionResult] = List(MessageResult(s"The ${monster.displayName} casts a spell."))
    results = castSpell(monster, game, callback) :: results
    results
  }
  
  def castSpell(monster: Monster, game: GameAccessor, callback: GameCallback): ActionResult
}
