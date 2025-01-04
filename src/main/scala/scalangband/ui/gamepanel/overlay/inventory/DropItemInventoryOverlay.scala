package scalangband.ui.gamepanel.overlay.inventory

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.Game
import scalangband.model.item.{Item, StackableItem}
import scalangband.model.player.action.{DropInventoryItemAction, PlayerAction}
import scalangband.ui.gamepanel.overlay.equipment.{DropEquipmentOverlay, EquipmentOverlay}
import scalangband.ui.gamepanel.overlay.{AllItemFilter, GamePanelOverlay, ItemActionFactory, OverlayPane}
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Font, Graphics2D}

class DropItemInventoryOverlay(game: Game) extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new DropInventoryItemKeyHandler(this)

  override def panel: Option[OverlayPane] = Some(new InventoryPane(game, "Drop which item?", AllItemFilter))
}

class DropInventoryItemKeyHandler(overlay: DropItemInventoryOverlay) extends KeyHandler {
  private val actionFactory = DropItemActionFactory

  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

      case KeyPressed(_, Key.Slash, _, _) => Right(new DropEquipmentOverlay(game))

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
    if (inventory.size > idx) {
      inventory.getItem(idx) match {
        case stack: StackableItem if stack.quantity == 1 => Left(DropItemActionFactory(stack.clone(1)))
        case stack: StackableItem => Right(SelectQuantityOverlay(stack))
        case item: Item => Left(DropItemActionFactory(item))
      }
    } else {
      Right(overlay)
    }
  }
}

class SelectQuantityOverlay(stack: StackableItem, input: String = "") extends GamePanelOverlay {
  override def message: Option[String] = None
  override def keyHandler: KeyHandler = new SelectQuantityKeyHandler(stack, input)
  override def panel: Option[OverlayPane] = Some(new SelectQuantityPane(stack, input))
}

class SelectQuantityKeyHandler(stack: StackableItem, input: String) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

      case KeyPressed(_, Key.Asterisk, _, _) => handleInput('*')
      case KeyPressed(_, Key.Key8, Key.Modifier.Shift, _) => handleInput('*')

      case KeyPressed(_, Key.Key0, _, _) => handleInput('0')
      case KeyPressed(_, Key.Key1, _, _) => handleInput('1')
      case KeyPressed(_, Key.Key2, _, _) => handleInput('2')
      case KeyPressed(_, Key.Key3, _, _) => handleInput('3')
      case KeyPressed(_, Key.Key4, _, _) => handleInput('4')
      case KeyPressed(_, Key.Key5, _, _) => handleInput('5')
      case KeyPressed(_, Key.Key6, _, _) => handleInput('6')
      case KeyPressed(_, Key.Key7, _, _) => handleInput('7')
      case KeyPressed(_, Key.Key8, _, _) => handleInput('8')
      case KeyPressed(_, Key.Key9, _, _) => handleInput('9')

      case KeyPressed(_, Key.BackSpace, _, _) =>
        if (input.nonEmpty) {
          Right(new SelectQuantityOverlay(stack, input.substring(0, input.length - 1)))
        } else {
          Right(new SelectQuantityOverlay(stack, input))
        }

      case KeyPressed(_, Key.Enter, _, _) =>
        val quantity = input match {
          case "" => 1
          case "*" => stack.quantity
          case s: String =>
            val requested = s.toInt
            if (requested > stack.quantity) stack.quantity else requested
        }
        Left(DropItemActionFactory(stack.clone(quantity)))

      case _ => Right(new SelectQuantityOverlay(stack, input))
    }
  }

  private def handleInput(key: Char): Either[Option[PlayerAction], GamePanelOverlay] = {
    Right(new SelectQuantityOverlay(stack, input + key))
  }
}

class SelectQuantityPane(stack: StackableItem, input: String) extends OverlayPane {
  override def paint(g: Graphics2D, font: Font): Unit = {
    val fontMetrics = g.getFontMetrics(font)
    val lineHeight = fontMetrics.getHeight
    val charWidth = fontMetrics.charWidth(' ')

    g.setColor(White)
    g.drawString(s"Quantity (0, 0-${stack.quantity}, *=all): $input", 0, lineHeight)
  }
}

object DropItemActionFactory extends ItemActionFactory {
  override def apply(item: Item): Option[PlayerAction] = Some(new DropInventoryItemAction(item))
}
