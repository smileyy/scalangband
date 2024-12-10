package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessagesResult, NoResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}
import scalangband.model.{Game, GameAccessor, GameCallback}

object PendingDirectionCloseAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): PlayerAction = new CloseAction(direction)
}

class CloseAction(direction: Direction) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {

    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: OpenDoor =>
        callback.level.replaceTile(targetCoordinates, new ClosedDoor())
        List.empty
      case _: BrokenDoor => List(MessagesResult("The door appears to be broken."))
      case _ => List(MessagesResult("You see nothing there to close"))
    }

  }
}