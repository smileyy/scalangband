package scalangband.ui.render

import scalangband.model.level.DungeonLevel

trait Renderer {
  def render(level: DungeonLevel): Array[Array[RenderableTile]]
}
