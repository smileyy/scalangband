package scalangband.ui.gamepanel.overlay

import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.item.food.Food
import scalangband.model.player.action.{EatFoodAction, PlayerAction}
import scalangband.ui.gamepanel.overlay.inventory.InventoryOverlay

object EatFoodOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, "Eat which food?", FoodFilter, EatFoodActionFactory)
  }
}

object EatFoodActionFactory extends ItemActionFactory {
  override def apply(item: Item): Option[PlayerAction] = Some(new EatFoodAction(item.asInstanceOf[Food]))
}

object FoodFilter extends ItemFilter {
  override def apply(item: Item): Boolean = item.isInstanceOf[Food]
}

