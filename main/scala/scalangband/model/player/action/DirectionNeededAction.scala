package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.location.Direction
import scalangband.model.{Game, GameAccessor, GameCallback}

trait DirectionNeededAction extends InterfaceAction {
  def withDirection(direction: Direction): PlayerAction

  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    List(MessageResult("Choose a direction..."))
  }
}