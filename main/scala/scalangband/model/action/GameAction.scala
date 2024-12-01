package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.ActionResult

trait GameAction {
  def energyRequired: Int
  def apply(game: Game): Option[ActionResult]

  override def toString: String = getClass.getSimpleName
}
