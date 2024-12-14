package scalangband.model.player.action

import org.slf4j.LoggerFactory
import scalangband.model.player.action.PlayerMovementAction.Logger
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.effect.Confusion
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.tile.*
import scalangband.model.{GameAccessor, GameCallback}

object PlayerPassAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = List.empty
}

object GoDownStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case _: DownStairs =>
        callback.moveDownTo(accessor.level.depth + 1)
        List(MessageResult(s"You enter a maze of down staircases."))
      case _ => List(MessageResult("I see no down staircase here."))
    }
  }
}

object GoUpStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case _: UpStairs =>
        callback.moveUpTo(accessor.level.depth - 1)
        val message = "You enter a maze of up staircases."
        List(MessageResult(message))
      case _ => List(MessageResult("I see no up staircase here."))
    }
  }
}

object PickUpItemAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case floor: Floor if floor.items.nonEmpty =>
        val item = floor.items.head
        callback.playerPickup(floor, floor.items.head)
        List(MessageResult(s"You pick up the ${item.displayName}."))
      case _ => List(MessageResult("There is nothing to pick up."))
    }
  }
}

object ListInventoryAction extends InterfaceAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.logInventory()
    List.empty
  }
}

object ListEquipmentAction extends InterfaceAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.logEquipment()
    List.empty
  }
}