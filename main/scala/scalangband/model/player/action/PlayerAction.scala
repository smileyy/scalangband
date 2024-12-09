package scalangband.model.player.action

import scalangband.model.action.ActionResult
import scalangband.model.{Game, GameAccessor, GameCallback}

trait PlayerAction {
  def energyRequired: Int
  def apply(accessor: GameAccessor, callback: GameCallback): ActionResult

  override def toString: String = getClass.getSimpleName
}
