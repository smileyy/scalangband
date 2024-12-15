package scalangband.ui.gamepanel.overlay

import scalangband.bridge.rendering.TextColors
import scalangband.model.Game
import scalangband.model.player.action.PlayerAction
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Font, Graphics2D}

class CharacterOverlay(game: Game) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new CharacterOverlayKeyHandler(this)
  override def panel: Option[OverlayPanel] = Some(new CharacterPane(game))
}

class CharacterOverlayKeyHandler(overlay: CharacterOverlay) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) =>
        println("Escape")
        Left(None)
      case _ => Right(overlay)
    }
  }
}

class CharacterPane(game: Game) extends OverlayPanel {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    g.setColor(TextColors.Black)
    g.fillRect(0, 0, 1024, 768)

    g.setFont(font)
    g.setColor(TextColors.White)
    g.drawString(s"Name: ${game.player.name}", charWidth, lineHeight * 2)
  }
}