package scalangband.ui.gamepanel.overlay

import scalangband.bridge.rendering.TextColors.*
import scalangband.model.Game
import scalangband.model.item.Item
import scalangband.model.item.food.Food
import scalangband.model.player.action.{DropInventoryItemAction, EatFoodAction, PlayerAction}
import scalangband.ui.gamepanel.{GamePanel, PlayerPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Font, Graphics2D}

class InventoryOverlay(
    game: Game,
    factory: InventoryActionFactory,
    prompt: String,
    filter: InventoryFilter = AllInventoryFilter
) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new InventoryKeyHandler(game, factory, filter, this)

  override def panel: Option[OverlayPanel] = Some(new InventoryPane(game, prompt, filter))
}

object InventoryListOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, ViewItemActionFactory, "Select Item: ")
  }
}

object DropItemInventoryOverlay {
  def apply(game: Game): InventoryOverlay = {
    new InventoryOverlay(game, DropItemActionFactory, "Drop which item?")
  }
}

class InventoryKeyHandler(game: Game, factory: InventoryActionFactory, filter: InventoryFilter, overlay: InventoryOverlay) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

      case KeyPressed(_, Key.Slash, _, _) => Right(new EquipmentOverlay(game))

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
      val item = inventory.getItem(idx)
      Left(factory(item))
    } else {
      Right(overlay)
    }
  }
}

class InventoryPane(game: Game, prompt: String, filter: InventoryFilter) extends OverlayPanel {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    val itemsByCharacter = game.player.inventory.items.zipWithIndex.filter((item, idx) => filter(item))

    g.setColor(Black)
    val startX = (PlayerPane.WidthInChars + 1) * charWidth
    g.fillRect(startX, 0, GamePanel.WidthInPixels - startX, (itemsByCharacter.size + 1) * lineHeight)

    g.setColor(White)
    g.drawString(prompt, 0, lineHeight)
    var line = 0
    itemsByCharacter.foreach { (item, idx) =>
      g.drawString(s"${(idx + 'a').toChar}) a ${item.displayName}", startX, (line + 2) * lineHeight)
      line = line + 1
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

trait InventoryFilter {
  def apply(item: Item): Boolean
  def accepts(item: Item): Boolean = apply(item)
}

object AllInventoryFilter extends InventoryFilter {
  override def apply(item: Item): Boolean = true
}