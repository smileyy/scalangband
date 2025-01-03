package scalangband.ui.gamepanel.overlay

import scalangband.model.Game
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.player.action.{PlayerAction, WearEquipmentAction}

object WearEquipmentOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, WearEquipmentActionFactory, "Wear or wield which item?", EquipmentFilter)
  }
}

object WearEquipmentActionFactory extends InventoryActionFactory {
  override def apply(item: Item): Option[PlayerAction] = {
    Some(new WearEquipmentAction(item.asInstanceOf[EquippableItem]))
  }
}

object EquipmentFilter extends InventoryFilter {
  override def apply(item: Item): Boolean = item.isInstanceOf[EquippableItem]
}