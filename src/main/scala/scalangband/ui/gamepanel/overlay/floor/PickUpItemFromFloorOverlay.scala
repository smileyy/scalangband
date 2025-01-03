package scalangband.ui.gamepanel.overlay.floor

import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.player.action.{PickUpItemAction, PlayerAction}
import scalangband.model.tile.Floor
import scalangband.ui.gamepanel.overlay.ItemActionFactory

object PickUpItemFromFloorOverlay {
  def apply(game: Game, tile: Floor): FloorItemOverlay = {
    new FloorItemOverlay(game, tile, "Get which item?", factory = PickUpItemActionFactory)
  }
}

object PickUpItemActionFactory extends ItemActionFactory {
  override def apply(item: Item): Option[PlayerAction] = Some(new PickUpItemAction(item))
}