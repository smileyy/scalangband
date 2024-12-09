package scalangband.ui.keys

import scalangband.model.Game
import scalangband.model.location.*
import scalangband.model.player.action.{GoDownStairsAction, GoUpStairsAction, ListEquipmentAction, ListInventoryAction, MovementAction, PendingDirectionCloseAction, PendingDirectionOpenAction, PickUpItemAction, PlayerAction, PlayerPassAction}
import scalangband.ui.GamePanelCallback

import scala.swing.event.{Key, KeyPressed}

object MainKeyHandler extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game, callback: GamePanelCallback): Option[PlayerAction] = event match {
    case KeyPressed(_, Key.Key1, _, _) => Some(MovementAction(DownLeft))
    case KeyPressed(_, Key.Key2, _, _) => Some(MovementAction(Down))
    case KeyPressed(_, Key.Key3, _, _) => Some(MovementAction(DownRight))
    case KeyPressed(_, Key.Key4, _, _) => Some(MovementAction(Left))
    case KeyPressed(_, Key.Key5, _, _) => Some(PlayerPassAction)
    case KeyPressed(_, Key.Key6, _, _) => Some(MovementAction(Right))
    case KeyPressed(_, Key.Key7, _, _) => Some(MovementAction(UpLeft))
    case KeyPressed(_, Key.Key8, _, _) => Some(MovementAction(Up))
    case KeyPressed(_, Key.Key9, _, _) => Some(MovementAction(UpRight))
    case KeyPressed(_, Key.Up, _, _) => Some(MovementAction(Up))
    case KeyPressed(_, Key.Down, _, _) => Some(MovementAction(Down))
    case KeyPressed(_, Key.Left, _, _) => Some(MovementAction(Left))
    case KeyPressed(_, Key.Right, _, _) => Some(MovementAction(Right))
    case KeyPressed(_, Key.Period, Key.Modifier.Shift, _) => Some(GoDownStairsAction)
    case KeyPressed(_, Key.Comma, Key.Modifier.Shift, _) => Some(GoUpStairsAction)

    case KeyPressed(_, Key.C, _, _) =>
      val action = PendingDirectionCloseAction
      callback.pushKeyHandler(new DirectionKeyHandler(action))
      Some(action)
    case KeyPressed(_, Key.E, _, _) => Some(ListEquipmentAction)
    case KeyPressed(_, Key.G, _, _) => Some(PickUpItemAction)
    case KeyPressed(_, Key.I, _, _) => Some(ListInventoryAction)
    case KeyPressed(_, Key.O, _, _) =>
      val action = PendingDirectionOpenAction
      callback.pushKeyHandler(new DirectionKeyHandler(action))
      Some(action)

    case KeyPressed(_, Key.W, Key.Modifier.Control, _) =>
      game.enableDebug()
      callback.repaint()
      None

    case KeyPressed(_, _, _, _) => None
  }
}
