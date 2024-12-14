package scalangband.ui.gamepanel

import scalangband.bridge.rendering.TextColors.White

import scala.swing.{Font, Graphics2D}

class MessagePane(font: Font, var messages: List[String] = List.empty) {
  def paint(g: Graphics2D, x: Int, y: Int): Unit = {
    val lineHeight = g.getFontMetrics(font).getHeight

    if (messages.nonEmpty) {
      val line = StringBuilder()
      while (messages.nonEmpty && line.length() + 1 + messages.head.length <= MessagePane.MaxMessageLineLength) {
        if (line.nonEmpty) line.append(' ')
        line.append(messages.head)
        messages = messages.tail
      }

      g.setColor(White)
      g.drawString(line.toString, x, y + lineHeight)
    }
  }
}

object MessagePane {
  private val MaxMessageLineLength = 96
}
