package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessagesResult, NoResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}
import scalangband.model.{Game, GameAccessor, GameCallback}

object PendingDirectionOpenAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): PlayerAction = new OpenAction(direction)
}

class OpenAction(direction: Direction) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): ActionResult = {

    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: ClosedDoor => 
        callback.level.replaceTile(targetCoordinates, new OpenDoor())
        NoResult
      case _: BrokenDoor => MessagesResult(List("The door is broken"), false)
      case _ => MessagesResult(List("There is nothing to open there"), false)
    }

  }
}