package scalangband.model.action.monster

import scalangband.model.action.result.ActionResult
import scalangband.model.monster.Monster
import scalangband.model.{Game, GameAccessor, GameCallback}

trait MonsterAction {
  /**
   * All monster actions should require the base unit of energy...for now.
   */
  def energyRequired: Int = Game.BaseEnergyUnit

  def apply(monster: Monster, game: GameAccessor, callback: GameCallback): Option[ActionResult]

  override def toString: String = getClass.getSimpleName
}
