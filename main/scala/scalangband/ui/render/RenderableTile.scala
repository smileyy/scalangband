package scalangband.ui.render

import scala.swing.Graphics2D

trait RenderableTile {
  def render(g: Graphics2D, x: Int, y: Int, col: Int, row: Int): Unit
}
