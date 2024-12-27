package scalangband.ui.render

import scalangband.model.level.DungeonLevel
import scalangband.model.player.Player

trait Renderer {
  def render(player: Player, level: DungeonLevel): Array[Array[RenderableTile]]
}
