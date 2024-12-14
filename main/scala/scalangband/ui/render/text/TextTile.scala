package scalangband.ui.render.text

import scalangband.ui.render.RenderableTile

import scala.swing.{Color, Font, Graphics2D}

case class TextTile(char: Character, font: Font, color: Color) extends RenderableTile {
  def render(g: Graphics2D, x: Int, y: Int, col: Int, row: Int): Unit = {
    val metrics = g.getFontMetrics(font)
    val lineHeight = metrics.getHeight - 1
    val charWidth = metrics.charWidth(' ') + 1

    g.setColor(color)
    g.setFont(font)
    g.drawChars(Array(char), 0, 1, x + (col * charWidth), y + (row * lineHeight))
  }
}
