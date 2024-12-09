package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessagesResult, NoResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}
import scalangband.model.{Game, GameAccessor, GameCallback}

object PendingDirectionCloseAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): PlayerAction = new CloseAction(direction)
}

class CloseAction(direction: Direction) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): ActionResult = {

    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: OpenDoor =>
        callback.level.replaceTile(targetCoordinates, new ClosedDoor())
        NoResult
      case _: BrokenDoor => MessagesResult(List("The door appears to be broken."), false)
      case _ => MessagesResult(List("You see nothing there to close"), false)
    }

  }
}