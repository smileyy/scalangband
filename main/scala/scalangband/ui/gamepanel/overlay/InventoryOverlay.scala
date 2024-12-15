package scalangband.ui.gamepanel.overlay

import scalangband.bridge.rendering.TextColors.*
import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.player.action.{DropInventoryItemAction, PlayerAction}
import scalangband.ui.gamepanel.{GamePanel, PlayerPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.{Font, Graphics2D}
import scala.swing.event.{Key, KeyPressed}

class InventoryOverlay(game: Game, factory: InventoryActionFactory, prompt: String) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new InventoryKeyHandler(game, factory, this)

  override def panel: Option[OverlayPanel] = Some(new InventoryPane(game, prompt))
}

class InventoryKeyHandler(game: Game, factory: InventoryActionFactory, overlay: InventoryOverlay) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)
      
      case KeyPressed(_, Key.A, _, _) => actionOrOverlay(game, 0)
      case KeyPressed(_, Key.B, _, _) => actionOrOverlay(game, 1)
      case KeyPressed(_, Key.C, _, _) => actionOrOverlay(game, 2)
      case KeyPressed(_, Key.D, _, _) => actionOrOverlay(game, 3)
      case KeyPressed(_, Key.E, _, _) => actionOrOverlay(game, 4)
      case KeyPressed(_, Key.F, _, _) => actionOrOverlay(game, 5)
      case KeyPressed(_, Key.G, _, _) => actionOrOverlay(game, 6)
      case KeyPressed(_, Key.H, _, _) => actionOrOverlay(game, 7)
      case KeyPressed(_, Key.I, _, _) => actionOrOverlay(game, 8)
      case KeyPressed(_, Key.J, _, _) => actionOrOverlay(game, 9)
      case KeyPressed(_, Key.K, _, _) => actionOrOverlay(game, 10)
      case KeyPressed(_, Key.L, _, _) => actionOrOverlay(game, 11)
      case KeyPressed(_, Key.M, _, _) => actionOrOverlay(game, 12)
      case KeyPressed(_, Key.N, _, _) => actionOrOverlay(game, 13)
      case KeyPressed(_, Key.O, _, _) => actionOrOverlay(game, 14)
      case KeyPressed(_, Key.P, _, _) => actionOrOverlay(game, 15)
      case KeyPressed(_, Key.Q, _, _) => actionOrOverlay(game, 16)
      
      case _ => Right(overlay)
    }
  }

  private def actionOrOverlay(game: Game, idx: Int): Either[Option[PlayerAction], GamePanelOverlay] = {
    if (game.player.inventory.size > idx) {
      val item = game.player.inventory.getItem(idx)
      Left(factory(item))
    } else {
      Right(overlay)
    }
  }
}

class InventoryPane(game: Game, prompt: String) extends OverlayPanel {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    val itemsByCharacter = game.player.inventory.items.zipWithIndex

    g.setColor(Black)
    val startX = (PlayerPane.WidthInChars + 1) * charWidth
    g.fillRect(startX, 0, GamePanel.WidthInPixels - startX, (itemsByCharacter.size + 1) * lineHeight)

    g.setColor(White)
    g.drawString(prompt, 0, lineHeight)
    itemsByCharacter.foreach { (item, idx) =>
      g.drawString(s"${(idx + 'a').toChar}) a ${item.displayName}", startX, (idx + 2) * lineHeight)
    }
  }
}

trait InventoryActionFactory {
  def apply(item: Item): Option[PlayerAction]
}

object DropItemActionFactory extends InventoryActionFactory {
  override def apply(item: Item): Option[PlayerAction] = Some(new DropInventoryItemAction(item))
}

object ViewItemActionFactory extends InventoryActionFactory {
  override def apply(item: Item): Option[PlayerAction] = None
}