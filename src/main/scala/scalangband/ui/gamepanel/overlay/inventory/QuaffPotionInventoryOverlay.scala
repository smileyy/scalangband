package scalangband.ui.gamepanel.overlay.inventory

import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.item.potion.Potion
import scalangband.model.player.action.{PlayerAction, QuaffPotionAction}
import scalangband.ui.gamepanel.overlay.*

object QuaffPotionOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, "Quaff which potion?", PotionFilter, QuaffPotionActionFactory)
  }
}

object QuaffPotionActionFactory extends ItemActionFactory {
  override def apply(item: Item): Option[PlayerAction] = {
    Some(QuaffPotionAction(item.asInstanceOf[Potion], true))
  }
}

object PotionFilter extends ItemFilter {
  override def apply(item: Item): Boolean = item.isInstanceOf[Potion]
}
