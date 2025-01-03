package scalangband.model.player.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.item.potion.Potion
import scalangband.model.{GameAccessor, GameCallback}

class QuaffPotionAction(potion: Potion, fromInventory: Boolean) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.quaff(potion, fromInventory)
  }
}