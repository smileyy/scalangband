package scalangband.model.monster.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.monster.Monster
import scalangband.model.{Game, GameAccessor, GameCallback}

trait MonsterAction {
  /**
   * All monster actions should require the base unit of energy...for now.
   */
  def energyRequired: Int = Game.BaseEnergyUnit

  def apply(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult]

  override def toString: String = getClass.getSimpleName
}
