package scalangband.ui.gamepanel.overlay

import scalangband.bridge.rendering.TextColors.{Black, White}
import scalangband.model.Game
import scalangband.model.item.EquippableItem
import scalangband.model.player.action.PlayerAction
import scalangband.ui.gamepanel.{GamePanel, PlayerPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.{Font, Graphics2D}
import scala.swing.event.{Key, KeyPressed}

class EquipmentOverlay(game: Game) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new EquipmentKeyHandler(this)
  override def panel: Option[OverlayPanel] = Some(new EquipmentPanel(game))
}

class EquipmentKeyHandler(overlay: GamePanelOverlay) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)
      case _ => Right(overlay)
    }
  }
}

class EquipmentPanel(game: Game) extends OverlayPanel {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    val equipment = game.player.equipment
    val numberOfEquipmentItems: Int = 3

    g.setColor(Black)
    val startX = (PlayerPane.WidthInChars + 1) * charWidth
    g.fillRect(startX, 0, GamePanel.WidthInPixels - startX, (numberOfEquipmentItems + 1) * lineHeight)

    g.setColor(White)
    g.drawString("Select Item:", 0, lineHeight)

    def drawItem(option: Char, label: String, maybeItem: Option[EquippableItem], line: Int): Unit = {
      maybeItem match {
        case Some(item) => g.drawString(s"$option) $label: ${item.article}${item.displayName}", startX, (line + 2) * lineHeight)
        case None => g.drawString(s" ) $label: (nothing)", startX, (line + 2) * lineHeight)
      }
    }

    drawItem('a', "Wielding", equipment.weapon, 0)
    drawItem('f', "Holding", equipment.light, 1)
    drawItem('g', "Wearing", equipment.body, 2)
  }
}