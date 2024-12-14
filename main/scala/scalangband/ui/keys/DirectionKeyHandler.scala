package scalangband.ui.keys

import scalangband.model.Game
import scalangband.model.location.*
import scalangband.model.player.action.{DirectionNeededAction, PlayerAction}
import scalangband.ui.gamepanel.GamePanelCallback

import scala.swing.event.{Key, KeyPressed}

class DirectionKeyHandler(action: DirectionNeededAction) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game, callback: GamePanelCallback): Option[PlayerAction] = {
    val result = event match {
      case KeyPressed(_, Key.Key1, _, _) => Some(action.withDirection(DownLeft))
      case KeyPressed(_, Key.Key2, _, _) => Some(action.withDirection(Down))
      case KeyPressed(_, Key.Key3, _, _) => Some(action.withDirection(DownRight))
      case KeyPressed(_, Key.Key4, _, _) => Some(action.withDirection(Left))
      case KeyPressed(_, Key.Key6, _, _) => Some(action.withDirection(Right))
      case KeyPressed(_, Key.Key7, _, _) => Some(action.withDirection(UpLeft))
      case KeyPressed(_, Key.Key8, _, _) => Some(action.withDirection(Up))
      case KeyPressed(_, Key.Key9, _, _) => Some(action.withDirection(UpRight))
      case KeyPressed(_, Key.Up, _, _) => Some(action.withDirection(Up))
      case KeyPressed(_, Key.Down, _, _) => Some(action.withDirection(Down))
      case KeyPressed(_, Key.Left, _, _) => Some(action.withDirection(Left))
      case KeyPressed(_, Key.Right, _, _) => Some(action.withDirection(Right))
      case KeyPressed(_, _, _, _) => None
    }

    callback.popKeyHandler()
    result
  }
    
}
