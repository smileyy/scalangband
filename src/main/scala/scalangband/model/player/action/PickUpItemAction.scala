package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.item.{Item, StackableItem}
import scalangband.model.tile.Floor
import scalangband.model.{GameAccessor, GameCallback}

class PickUpItemAction(item: Item) extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case floor: Floor if floor.items.nonEmpty =>
        val item = floor.items.head
        val itemToAdd = item match {
          case stackable: StackableItem => stackable.clone(stackable.quantity)
          case regularItem: Item => regularItem
        }
        callback.player.addToInventory(itemToAdd)
        floor.removeItem(item)
        List(MessageResult(s"You pick up $item."))
      case _ => List(MessageResult("There is nothing to pick up."))
    }
  }
}

object NothingToPickUpAction extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    List(MessageResult("There is nothing to pick up."))
  }
}
