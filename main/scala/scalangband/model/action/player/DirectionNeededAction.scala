package scalangband.model.action.player

import scalangband.model.action.player.PlayerAction
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Direction
import scalangband.model.{Game, GameAccessor, GameCallback}

trait DirectionNeededAction extends InterfaceAction {
  def withDirection(direction: Direction): PlayerAction

  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    Some(MessagesResult(List("Choose a direction...")))
  }
}