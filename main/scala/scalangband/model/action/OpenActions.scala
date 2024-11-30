package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}

object PendingDirectionOpenAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): GameAction = new OpenAction(direction)
}

class OpenAction(direction: Direction) extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = {

    val targetCoordinates = game.playerCoordinates + direction
    val targetTile = game.level(targetCoordinates)

    targetTile match {
      case _: ClosedDoor => 
        game.level.replaceTile(targetCoordinates, new OpenDoor(targetCoordinates, None))
        None
      case _: BrokenDoor => Some(MessageResult("The door is broken"))
      case _ => Some(MessageResult("There is nothing to open there"))
    }

  }
}