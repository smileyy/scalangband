package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult, TrivialResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{ClosedDoor, OccupiableTile, OpenDoor, Wall}

case class MovementAction(direction: Direction) extends GameAction {
  def apply(game: Game): ActionResult = {
    val targetCoordinates = game.playerCoordinates + direction
    val targetTile = game.level(targetCoordinates)

    targetTile match {
      case _: Wall => MessageResult("There is a wall in the way")
      case cd: ClosedDoor => 
        game.level.setTile(targetCoordinates, new OpenDoor(targetCoordinates, None))
        TrivialResult
      case ot: OccupiableTile if ot.occupied => MessageResult("Something's in the way?!")
      case ot: OccupiableTile =>
        game.playerTile.clearOccupant()
        ot.setOccupant(game.player)
        game.playerCoordinates = ot.coordinates
        TrivialResult
    }
  }
}
