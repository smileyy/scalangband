package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult, TrivialResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}

object PendingDirectionCloseAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): GameAction = new CloseAction(direction)
}

class CloseAction(direction: Direction) extends GameAction {
  override def apply(game: Game): Option[ActionResult] = {

    val targetCoordinates = game.playerCoordinates + direction
    val targetTile = game.level(targetCoordinates)

    targetTile match {
      case _: OpenDoor =>
        game.level.setTile(targetCoordinates, new ClosedDoor(targetCoordinates))
        None
      case _: ClosedDoor => Some(MessageResult("The door is already closed"))
      case _: BrokenDoor => Some(MessageResult("The door is broken"))
      case _ => Some(MessageResult("There is nothing to close there"))
    }

  }
}