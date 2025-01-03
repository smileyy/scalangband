package scalangband.ui.gamepanel.overlay

import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.item.potion.Potion
import scalangband.model.player.action.{PlayerAction, QuaffPotionAction}

object QuaffPotionOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, QuaffPotionActionFactory, "Quaff which potion?", PotionFilter)
  }
}

object QuaffPotionActionFactory extends InventoryActionFactory {
  override def apply(item: Item): Option[PlayerAction] = {
    Some(QuaffPotionAction(item.asInstanceOf[Potion], true))
  }
}

object PotionFilter extends InventoryFilter {
  override def apply(item: Item): Boolean = item.isInstanceOf[Potion]
}
