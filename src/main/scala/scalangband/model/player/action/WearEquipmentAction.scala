package scalangband.model.player.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.item.EquippableItem
import scalangband.model.{GameAccessor, GameCallback}

class WearEquipmentAction(item: EquippableItem) extends PhysicalAction {

  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.equip(item)
  }
}
