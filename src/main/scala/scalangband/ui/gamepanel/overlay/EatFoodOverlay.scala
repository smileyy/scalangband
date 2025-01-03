package scalangband.ui.gamepanel.overlay

import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.item.food.Food
import scalangband.model.player.action.{EatFoodAction, PlayerAction}

object EatFoodOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, EatFoodActionFactory, "Eat which food?", FoodFilter)
  }
}

object EatFoodActionFactory extends InventoryActionFactory {
  override def apply(item: Item): Option[PlayerAction] = Some(new EatFoodAction(item.asInstanceOf[Food]))
}

object FoodFilter extends InventoryFilter {
  override def apply(item: Item): Boolean = item.isInstanceOf[Food]
}

