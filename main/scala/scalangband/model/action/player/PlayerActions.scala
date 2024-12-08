package scalangband.model.action.player

import org.slf4j.LoggerFactory
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.tile.*
import scalangband.model.{GameAccessor, GameCallback}

import java.util.logging.Logger

object PlayerPassAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = None
}

case class MovementAction(direction: Direction) extends PhysicalAction {
  def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    val targetCoordinates = accessor.player.coordinates + direction
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: Wall => Some(MessagesResult(List("There is a wall in the way!"), false))
      case _: ClosedDoor =>
        callback.level.replaceTile(targetCoordinates, new OpenDoor())
        None
      case ot: OccupiableTile if ot.occupied =>
        Some(callback.player.attack(ot.occupant.get.asInstanceOf[Monster], callback))
      case floor: Floor if floor.items.nonEmpty =>
        var messages: List[String] = List.empty
        callback.movePlayerTo(targetCoordinates)

        floor.money.foreach(money => {
          floor.removeItem(money)
          callback.player.addMoney(money.amount)
          messages = s"You pick up ${money.displayName} worth ${money.amount} gold." :: messages
        })

        if (floor.items.isEmpty) {} // it is possible that the floor only had money in it, and now it is all gone
          else if (floor.items.size == 1) messages = s"You see ${floor.items.head.displayName}." :: messages
          else messages = "You see a pile of items." :: messages

        Some(MessagesResult(messages.reverse))
      case ot: OccupiableTile =>
        callback.movePlayerTo(targetCoordinates)
        None
    }
  }

  override def toString: String = s"${getClass.getSimpleName}($direction)"
}

object GoDownStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case _: DownStairs =>
        callback.moveDownTo(accessor.level.depth + 1)
        Some(MessagesResult(List(s"You enter a maze of down staircases.")))
      case _ => Some(MessagesResult(List("I see no down staircase here."), false))
    }
  }
}

object GoUpStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case _: UpStairs =>
        callback.moveUpTo(accessor.level.depth - 1)
        val message = "You enter a maze of up staircases."
        Some(MessagesResult(List(message)))
      case _ => Some(MessagesResult(List("I see no up staircase here."), false))
    }
  }
}

object PickUpItemAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case floor: Floor if floor.items.nonEmpty =>
        val item = floor.items.head
        callback.playerPickup(floor, floor.items.head)
        Some(MessagesResult(List(s"You pick up the ${item.name}")))
      case _ => Some(MessagesResult(List("There is nothing to pick up"), false))
    }
  }
}

object ListInventoryAction extends InterfaceAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    callback.player.logInventory()
    None
  }
}

object ListEquipmentAction extends InterfaceAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    callback.player.logEquipment()
    None
  }
}