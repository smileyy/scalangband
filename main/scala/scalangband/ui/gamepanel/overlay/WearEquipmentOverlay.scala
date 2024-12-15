package scalangband.ui.gamepanel.overlay

import scalangband.bridge.rendering.TextColors.{Black, White}
import scalangband.model.Game
import scalangband.model.item.weapon.Weapon
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.player.action.{PlayerAction, WearEquipmentAction}
import scalangband.ui.gamepanel.{GamePanel, PlayerPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Font, Graphics2D}

class WearEquipmentOverlay(game: Game) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new WearEquipmentKeyHandler(game, this)
  override def panel: Option[OverlayPanel] = Some(new WearEquipmentPane(game))
}

class WearEquipmentKeyHandler(game: Game, overlay: WearEquipmentOverlay) extends KeyHandler {
  val equipmentItems: Map[Char, EquippableItem] = game.player.inventory.items
    .zipWithIndex
    .filter((item, _) => item.isInstanceOf[EquippableItem])
    .map((item, index) => (item.asInstanceOf[EquippableItem], index))
    .map((item, index) => ((index + 'a').toChar, item))
    .toMap

  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

      case KeyPressed(_, Key.A, _, _) => actionOrOverlay(equipmentItems, 'a')
      case KeyPressed(_, Key.B, _, _) => actionOrOverlay(equipmentItems, 'b')
      case KeyPressed(_, Key.C, _, _) => actionOrOverlay(equipmentItems, 'c')
      case KeyPressed(_, Key.D, _, _) => actionOrOverlay(equipmentItems, 'd')
      case KeyPressed(_, Key.E, _, _) => actionOrOverlay(equipmentItems, 'e')
      case KeyPressed(_, Key.F, _, _) => actionOrOverlay(equipmentItems, 'f')
      case KeyPressed(_, Key.G, _, _) => actionOrOverlay(equipmentItems, 'g')
      case KeyPressed(_, Key.H, _, _) => actionOrOverlay(equipmentItems, 'h')
      case KeyPressed(_, Key.I, _, _) => actionOrOverlay(equipmentItems, 'i')
      case KeyPressed(_, Key.J, _, _) => actionOrOverlay(equipmentItems, 'j')
      case KeyPressed(_, Key.K, _, _) => actionOrOverlay(equipmentItems, 'k')
      case KeyPressed(_, Key.L, _, _) => actionOrOverlay(equipmentItems, 'l')
      case KeyPressed(_, Key.M, _, _) => actionOrOverlay(equipmentItems, 'm')
      case KeyPressed(_, Key.N, _, _) => actionOrOverlay(equipmentItems, 'n')
      case KeyPressed(_, Key.O, _, _) => actionOrOverlay(equipmentItems, 'o')
      case KeyPressed(_, Key.P, _, _) => actionOrOverlay(equipmentItems, 'p')
      case KeyPressed(_, Key.Q, _, _) => actionOrOverlay(equipmentItems, 'q')

      case _ => Right(overlay)
    }
  }

  private def actionOrOverlay(items: Map[Char, EquippableItem], option: Char): Either[Option[PlayerAction], GamePanelOverlay] = {
    items.get(option) match {
      case Some(item) => Left(Some(new WearEquipmentAction(item)))
      case None => Right(overlay)
    }
  }
}

class WearEquipmentPane(game: Game) extends OverlayPanel {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    val equipmentItems: Seq[(Char, EquippableItem)] = game.player.inventory.items
      .zipWithIndex
      .filter((item, _) => item.isInstanceOf[EquippableItem])
      .map((item, index) => (item.asInstanceOf[EquippableItem], index))
      .map((item, index) => ((index + 'a').toChar, item))
      .toSeq

    g.setColor(Black)
    val startX = (PlayerPane.WidthInChars + 1) * charWidth
    g.fillRect(startX, 0, GamePanel.WidthInPixels - startX, (equipmentItems.size + 1) * lineHeight)

    g.setColor(White)
    g.drawString("Wear or wield which item?", 0, lineHeight)

    var itemsDrawn = 0
    equipmentItems.foreach((option, item) => {
      g.drawString(s"$option) ${item.displayName}", startX, (itemsDrawn + 2) * lineHeight)
      itemsDrawn = itemsDrawn + 1
    })
  }
}