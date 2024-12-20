package scalangband.model.player.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.item.Item
import scalangband.model.{GameAccessor, GameCallback}

class DropInventoryItemAction(item: Item) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.dropItem(item, callback)
  }
}
