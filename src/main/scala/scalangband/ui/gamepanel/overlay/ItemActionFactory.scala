package scalangband.ui.gamepanel.overlay

import scalangband.model.item.Item
import scalangband.model.player.action.PlayerAction

trait ItemActionFactory {
  def apply(item: Item): Option[PlayerAction]
}
