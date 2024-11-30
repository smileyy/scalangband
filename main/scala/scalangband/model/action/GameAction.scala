package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.ActionResult

trait GameAction {
  def apply(game: Game): Option[ActionResult]
}
