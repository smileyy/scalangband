package scalangband.model.player.action

import scalangband.model.action.{ActionResult, MessagesResult}
import scalangband.model.location.Direction
import scalangband.model.{Game, GameAccessor, GameCallback}

trait DirectionNeededAction extends InterfaceAction {
  def withDirection(direction: Direction): PlayerAction

  override def apply(accessor: GameAccessor, callback: GameCallback): ActionResult = {
    MessagesResult(List("Choose a direction..."))
  }
}