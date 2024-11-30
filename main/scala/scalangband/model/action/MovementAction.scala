package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.location.Direction
import scalangband.model.tile.{ClosedDoor, OccupiableTile, OpenDoor, Wall}

case class MovementAction(direction: Direction) extends PhysicalAction {
  def apply(game: Game): Option[ActionResult] = {
    val targetCoordinates = game.playerCoordinates + direction
    val targetTile = game.level(targetCoordinates)

    targetTile match {
      case _: Wall => Some(MessageResult("There is a wall in the way"))
      case _: ClosedDoor => 
        game.level.replaceTile(targetCoordinates, new OpenDoor())
        None
      case ot: OccupiableTile if ot.occupied => Some(MessageResult("Something's in the way?!"))
      case ot: OccupiableTile =>
        game.playerTile.clearOccupant()
        ot.setOccupant(game.player)
        game.playerCoordinates = targetCoordinates
        None
    }
  }

  override def toString: String = s"${getClass.getSimpleName}($direction)"
}
