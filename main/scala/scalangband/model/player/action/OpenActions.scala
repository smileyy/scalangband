package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessagesResult, NoResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}
import scalangband.model.{Game, GameAccessor, GameCallback}

object PendingDirectionOpenAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): PlayerAction = new OpenAction(direction)
}

class OpenAction(direction: Direction) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {

    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: ClosedDoor => 
        callback.level.replaceTile(targetCoordinates, new OpenDoor())
        List.empty
      case _: BrokenDoor => List(MessagesResult("The door is broken"))
      case _ => List(MessagesResult("There is nothing to open there"))
    }

  }
}