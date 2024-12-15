package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.tile.Floor
import scalangband.model.{GameAccessor, GameCallback}

object PickUpItemAction extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case floor: Floor if floor.items.nonEmpty =>
        val item = floor.items.head
        val result = callback.playerPickup(floor, floor.items.head)
        List(result)
      case _ => List(MessageResult("There is nothing to pick up."))
    }
  }
}
