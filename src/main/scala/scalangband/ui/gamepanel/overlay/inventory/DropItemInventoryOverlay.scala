package scalangband.ui.gamepanel.overlay.inventory

import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.player.action.{DropInventoryItemAction, PlayerAction}
import scalangband.ui.gamepanel.overlay.ItemActionFactory


object DropItemInventoryOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, "Drop which item?", factory = DropItemActionFactory)
  }
}
object DropItemActionFactory extends ItemActionFactory {
  override def apply(item: Item): Option[PlayerAction] = Some(new DropInventoryItemAction(item, 1))
}
