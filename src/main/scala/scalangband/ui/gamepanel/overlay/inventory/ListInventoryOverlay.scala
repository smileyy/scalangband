package scalangband.ui.gamepanel.overlay.inventory

import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.player.action.PlayerAction
import scalangband.ui.gamepanel.overlay.ItemActionFactory

object ListInventoryOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, "Select Item: ", factory = ViewItemActionFactory)
  }
}

object ViewItemActionFactory extends ItemActionFactory {
  override def apply(item: Item): Option[PlayerAction] = None
}
