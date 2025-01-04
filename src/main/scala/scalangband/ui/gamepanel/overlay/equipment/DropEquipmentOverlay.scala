package scalangband.ui.gamepanel.overlay.equipment

import scalangband.bridge.rendering.TextColors.{Black, White}
import scalangband.model.Game
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.player.Equipment
import scalangband.model.player.action.{DropEquipmentAction, PlayerAction}
import scalangband.ui.gamepanel.overlay.inventory.{DropItemActionFactory, DropItemInventoryOverlay}
import scalangband.ui.gamepanel.overlay.{GamePanelOverlay, OverlayPane}
import scalangband.ui.gamepanel.{GamePanel, PlayerPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Font, Graphics2D}

class DropEquipmentOverlay(game: Game) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new DropEquipmentKeyHandler(this)
  override def panel: Option[OverlayPane] = Some(new DropEquipmentPane(game))
}

class DropEquipmentKeyHandler(overlay: DropEquipmentOverlay) extends KeyHandler {
  private val actionFactory = DropItemActionFactory

  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

      case KeyPressed(_, Key.Slash, _, _) => Right(new DropItemInventoryOverlay(game))

      case KeyPressed(_, Key.A, _, _) => actionOrOverlay(game, e => e.weapon, e => e.unwieldWeapon())
      case KeyPressed(_, Key.B, _, _) => actionOrOverlay(game, e => e.light, e => e.removeLight())
      case KeyPressed(_, Key.C, _, _) => actionOrOverlay(game, e => e.body, e => e.removeBodyArmor())

      case _ => Right(overlay)
    }
  }

  private def actionOrOverlay(
      game: Game,
      get: Equipment => Option[EquippableItem],
      unequip: Equipment => Option[EquippableItem]
  ): Either[Option[PlayerAction], GamePanelOverlay] = {
    get(game.player.equipment) match {
      case Some(item) => Left(Some(DropEquipmentAction(unequip)))
      case None       => Right(overlay)
    }
  }
}

class DropEquipmentPane(game: Game) extends OverlayPane {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    val equipmentByIndex = game.player.equipment.allPossibleEquipment.zipWithIndex.filter((item, idx) => item.isDefined)

    g.setColor(Black)
    val startX = (PlayerPane.WidthInChars + 1) * charWidth
    g.fillRect(startX, 0, GamePanel.WidthInPixels - startX, (equipmentByIndex.size + 1) * lineHeight)

    g.setColor(White)
    g.drawString("Drop which item?", 0, lineHeight)
    var line = 0
    equipmentByIndex.foreach { (item, idx) =>
      g.drawString(s"${(idx + 'a').toChar}) ${item.get}", startX, (line + 2) * lineHeight)
      line = line + 1
    }
  }
}
