package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.location.Direction

trait DirectionNeededAction extends InterfaceAction {
  def withDirection(direction: Direction): GameAction

  override def apply(game: Game): Option[ActionResult] = Some(MessageResult("Choose a direction..."))
}