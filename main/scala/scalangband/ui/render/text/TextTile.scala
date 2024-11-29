package scalangband.ui.render.text

import scalangband.ui.render.RenderableTile

import scala.swing.{Color, Font, Graphics2D}

case class TextTile(char: Character, font: Font, color: Color) extends RenderableTile {
  def render(g: Graphics2D, x: Int, y: Int): Unit = {
    g.setColor(color)
    g.setFont(font)
    g.drawChars(Array(char), 0, 1, x, y)
  }
}
