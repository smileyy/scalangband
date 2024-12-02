package scalangband.ui.render

import scalangband.model.level.Level

trait Renderer {
  def tileWidth: Int
  def tileHeight: Int
  def render(level: Level): Array[Array[RenderableTile]]
}
