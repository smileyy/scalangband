package scalangband.ui.gamepanel.overlay.inventory

import scalangband.model.Game
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.player.action.{PlayerAction, WearEquipmentAction}
import scalangband.ui.gamepanel.overlay.{EquipmentFilter, ItemActionFactory}

object WearEquipmentInventoryOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, "Wear or wield which item?", EquipmentFilter, WearEquipmentActionFactory)
  }
}

object WearEquipmentActionFactory extends ItemActionFactory {
  override def apply(item: Item): Option[PlayerAction] = {
    Some(new WearEquipmentAction(item.asInstanceOf[EquippableItem]))
  }
}
