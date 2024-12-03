package scalangband.model.action

import scalangband.model.{Game, GameAccessor, GameCallback}
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.tile.{ClosedDoor, Floor, OccupiableTile, OpenDoor, Wall}

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
      case floor: Floor if floor.items.nonEmpty =>
        callback.movePlayerTo(targetCoordinates)
        val message = if (floor.items.size == 1) s"You see a ${floor.items.head.name}" else "You see a pile of items"
        Some(MessagesResult(List(message)))
      case ot: OccupiableTile =>
        callback.movePlayerTo(targetCoordinates)
        None
    }
  }

  override def toString: String = s"${getClass.getSimpleName}($direction)"
}
