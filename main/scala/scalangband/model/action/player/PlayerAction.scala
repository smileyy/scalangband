package scalangband.model.action.player

import scalangband.model.action.result.ActionResult
import scalangband.model.{Game, GameAccessor, GameCallback}

trait PlayerAction {
  def energyRequired: Int
  def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult]

  override def toString: String = getClass.getSimpleName
}
