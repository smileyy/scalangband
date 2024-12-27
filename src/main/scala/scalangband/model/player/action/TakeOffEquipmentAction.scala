package scalangband.model.player.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.item.Item
import scalangband.model.player.Equipment
import scalangband.model.{GameAccessor, GameCallback}

class TakeOffEquipmentAction(f: Equipment => Option[Item]) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.takeOff(f)
  }
}
