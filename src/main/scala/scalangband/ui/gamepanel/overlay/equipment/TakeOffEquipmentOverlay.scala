package scalangband.ui.gamepanel.overlay.equipment

import scalangband.bridge.rendering.TextColors.{Black, White}
import scalangband.model.Game
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.player.Equipment
import scalangband.model.player.action.{PlayerAction, TakeOffEquipmentAction}
import scalangband.ui.gamepanel.overlay.*
import scalangband.ui.gamepanel.{GamePanel, PlayerPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Font, Graphics2D}

class TakeOffEquipmentOverlay(game: Game) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new TakeOffEquipmentKeyHandler(game, this)
  override def panel: Option[OverlayPane] = Some(new TakeOffEquipmentPane(game))
}

class TakeOffEquipmentKeyHandler(game: Game, overlay: TakeOffEquipmentOverlay) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    val equipment = game.player.equipment

    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

      case KeyPressed(_, Key.A, _, _) =>
        equipmentActionOrOverlay(e => e.weapon, e => e.unwieldWeapon())
      case KeyPressed(_, Key.F, _, _) =>
        equipmentActionOrOverlay(e => e.light, e => e.removeLight())
      case KeyPressed(_, Key.G, _, _) =>
        equipmentActionOrOverlay(e => e.body, e => e.removeBodyArmor())

      case _ => Right(overlay)
    }
  }

  def equipmentActionOrOverlay(
      get: Equipment => Option[EquippableItem],
      takeOff: Equipment => Option[EquippableItem]
  ): Either[Option[PlayerAction], GamePanelOverlay] = get(game.player.equipment) match {
    case Some(item) => Left(Some(new TakeOffEquipmentAction(takeOff)))
    case None       => Right(overlay)
  }
}

class TakeOffEquipmentPane(game: Game) extends OverlayPane {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    val equipment = game.player.equipment
    val numberOfEquipmentItems: Int = equipment.allEquipment.size

    g.setColor(Black)
    val startX = (PlayerPane.WidthInChars + 1) * charWidth
    g.fillRect(startX, 0, GamePanel.WidthInPixels - startX, (numberOfEquipmentItems + 1) * lineHeight)

    g.setColor(White)
    g.drawString("Take off or unwield which item?", 0, lineHeight)

    var itemsDrawn = 0

    def paintEquipmentLine(get: Equipment => Option[Item], option: Char): Unit = {
      get(equipment) match {
        case Some(item) =>
          g.drawString(s"$option) $item", startX, (itemsDrawn + 2) * lineHeight)
          itemsDrawn = itemsDrawn + 1
        case None =>
      }
    }

    paintEquipmentLine(e => e.weapon, 'a')
    paintEquipmentLine(e => e.light, 'f')
    paintEquipmentLine(e => e.body, 'g')
  }
}
