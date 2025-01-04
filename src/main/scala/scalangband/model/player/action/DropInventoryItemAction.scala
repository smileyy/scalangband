package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.item.Item
import scalangband.model.{GameAccessor, GameCallback}

class DropInventoryItemAction(item: Item) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.removeFromInventory(item)
    val dropItemResult = callback.level.addItemToTile(accessor.player.coordinates, item)
    List(dropItemResult, MessageResult(s"You drop $item."))
  }
}
