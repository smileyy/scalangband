package scalangband.model.action

import scalangband.model.{Game, GameAccessor, GameCallback}
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.location.Direction

trait DirectionNeededAction extends InterfaceAction {
  def withDirection(direction: Direction): GameAction

  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    Some(MessageResult("Choose a direction..."))
  }
}