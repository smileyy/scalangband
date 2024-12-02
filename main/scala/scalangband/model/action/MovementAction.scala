package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.tile.{ClosedDoor, OccupiableTile, OpenDoor, Wall}

case class MovementAction(direction: Direction) extends PhysicalAction {
  def apply(game: Game): Option[ActionResult] = {
    val targetCoordinates = game.player.coordinates + direction
    val targetTile = game.level(targetCoordinates)

    targetTile match {
      case _: Wall => Some(MessageResult("There is a wall in the way"))
      case _: ClosedDoor => 
        game.level.replaceTile(targetCoordinates, new OpenDoor())
        None
      case ot: OccupiableTile if ot.occupied =>
        val result = game.player.attack(ot.occupant.get.asInstanceOf[Monster], game.callback)
        Some(result)
      case ot: OccupiableTile =>
        game.playerTile.clearOccupant()
        ot.setOccupant(game.player)
        game.player.coordinates = targetCoordinates
        None
    }
  }

  override def toString: String = s"${getClass.getSimpleName}($direction)"
}
