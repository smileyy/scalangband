package scalangband.model.action

import scalangband.model.{Game, GameAccessor, GameCallback}
import scalangband.model.action.result.ActionResult

trait GameAction {
  def energyRequired: Int
  def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult]

  override def toString: String = getClass.getSimpleName
}
