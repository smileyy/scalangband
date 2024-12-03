package scalangband.model.action

import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.tile.Floor

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