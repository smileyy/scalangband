package scalangband.ui.gamepanel.overlay.floor

import scalangband.bridge.rendering.TextColors.{Black, White}
import scalangband.model.Game
import scalangband.model.item.{Item, StackableItem}
import scalangband.model.player.action.PlayerAction
import scalangband.model.tile.Floor
import scalangband.ui.gamepanel.overlay.*
import scalangband.ui.gamepanel.{GamePanel, PlayerPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Font, Graphics2D}

class FloorItemOverlay(game: Game, tile: Floor, prompt: String, filter: ItemFilter = AllItemFilter, factory: ItemActionFactory)
    extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new FloorItemKeyHandler(game, factory, filter, this)
  override def panel: Option[OverlayPane] = Some(new FloorPane(tile, prompt, filter))
}

class FloorItemKeyHandler(game: Game, factory: ItemActionFactory, filter: ItemFilter, overlay: FloorItemOverlay)
  extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

//      case KeyPressed(_, Key.Slash, _, _) => Right(new EquipmentOverlay(game))

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
    val inventory = game.player.inventory
    if (inventory.size > idx && filter.accepts(inventory.getItem(idx))) {
      inventory.getItem(idx) match {
        case stackable: StackableItem => Left(factory(stackable.clone(stackable.quantity)))
        case item: Item => Left(factory(item))
      }
    } else {
      Right(overlay)
    }
  }
}

class FloorPane(tile: Floor, prompt: String, filter: ItemFilter) extends OverlayPane {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    val itemsByIndex = tile.items.zipWithIndex.filter((item, idx) => filter(item))

    g.setColor(Black)
    val startX = (PlayerPane.WidthInChars + 1) * charWidth
    g.fillRect(startX, 0, GamePanel.WidthInPixels - startX, (itemsByIndex.size + 1) * lineHeight)

    g.setColor(White)
    g.drawString(prompt, 0, lineHeight)
    var line = 0
    itemsByIndex.foreach { (item, idx) =>
      g.drawString(s"${(idx + 'a').toChar}) $item", startX, (line + 2) * lineHeight)
      line = line + 1
    }
  }
}