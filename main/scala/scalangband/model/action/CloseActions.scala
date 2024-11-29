package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult, TrivialResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{BrokenDoor, ClosedDoor, OpenDoor}

object PendingDirectionCloseAction extends DirectionNeededAction {
  override def withDirection(direction: Direction): GameAction = new CloseAction(direction)
}

class CloseAction(direction: Direction) extends GameAction {
  override def apply(game: Game): ActionResult = {

    val targetCoordinates = game.playerCoordinates + direction
    val targetTile = game.level(targetCoordinates)

    targetTile match {
      case _: OpenDoor =>
        game.level.setTile(targetCoordinates, new ClosedDoor(targetCoordinates))
        TrivialResult
      case _: ClosedDoor => MessageResult("The door is already closed")
      case _: BrokenDoor => MessageResult("The door is broken")
      case _ => MessageResult("There is nothing to close there")
    }

  }
}