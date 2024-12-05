package scalangband.model.action.player

import scalangband.model.action.player.{DirectionNeededAction, PlayerAction}
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}
import scalangband.model.{Game, GameAccessor, GameCallback}

object PendingDirectionOpenAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): PlayerAction = new OpenAction(direction)
}

class OpenAction(direction: Direction) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {

    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: ClosedDoor => 
        callback.level.replaceTile(targetCoordinates, new OpenDoor())
        None
      case _: BrokenDoor => Some(MessagesResult(List("The door is broken"), false))
      case _ => Some(MessagesResult(List("There is nothing to open there"), false))
    }

  }
}