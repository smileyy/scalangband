package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, TrivialResult}

object NoGameAction extends GameAction {
  override def apply(game: Game): ActionResult = TrivialResult
}
