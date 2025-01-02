package scalangband.model.monster.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.monster.Monster
import scalangband.model.player.PlayerAccessor

class HearingBoundedAction(hears: MonsterAction, alternate: MonsterAction) extends MonsterAction {
  override def apply(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = {
    if (canHearPlayer(monster, game.player)) {
      hears.apply(monster, game, callback)
    } else {
      alternate.apply(monster, game, callback)
    }
  }

  def canHearPlayer(monster: Monster, player: PlayerAccessor): Boolean = {
    monster.coordinates.euclidianDistanceTo(player.coordinates) <= monster.hearing
  }
}