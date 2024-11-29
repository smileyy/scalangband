package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, TrivialResult}

object PassAction extends GameAction {
  override def apply(game: Game): ActionResult = TrivialResult
}
