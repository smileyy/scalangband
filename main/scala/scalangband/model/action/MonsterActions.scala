package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}

case class TauntAction(taunt: String) extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = {
    Some(MessageResult(taunt))
  }
}