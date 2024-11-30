package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.ActionResult

object PassAction extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = None
}
