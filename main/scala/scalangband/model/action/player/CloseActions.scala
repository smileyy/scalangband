package scalangband.model.action.player

import scalangband.model.action.player.PlayerAction
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}
import scalangband.model.{Game, GameAccessor, GameCallback}

object PendingDirectionCloseAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): PlayerAction = new CloseAction(direction)
}

class CloseAction(direction: Direction) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {

    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: OpenDoor =>
        callback.level.replaceTile(targetCoordinates, new ClosedDoor())
        None
      case _: BrokenDoor => Some(MessagesResult(List("The door appears to be broken."), false))
      case _ => Some(MessagesResult(List("You see nothing there to close"), false))
    }

  }
}