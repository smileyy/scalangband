package scalangband.model.action

import scalangband.model.{Game, GameAccessor, GameCallback}
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.tile.{ClosedDoor, OccupiableTile, OpenDoor, Wall}

case class MovementAction(direction: Direction) extends PhysicalAction {
  def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: Wall => Some(MessagesResult(List("There is a wall in the way"), false))
      case _: ClosedDoor => 
        callback.level.replaceTile(targetCoordinates, new OpenDoor())
        None
      case ot: OccupiableTile if ot.occupied =>
        Some(callback.player.attack(ot.occupant.get.asInstanceOf[Monster], callback))
      case ot: OccupiableTile =>
        callback.movePlayerTo(targetCoordinates)
        None
    }
  }

  override def toString: String = s"${getClass.getSimpleName}($direction)"
}
