package scalangband.model.player.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.item.Item
import scalangband.model.{GameAccessor, GameCallback}

class DropInventoryItemAction(index: Int) extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    List(callback.player.dropItem(index, callback))
  }
}
