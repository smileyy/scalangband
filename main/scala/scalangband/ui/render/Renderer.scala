package scalangband.ui.render

import scalangband.model.level.Level

trait Renderer {
  def render(level: Level): Array[Array[RenderableTile]]
}
