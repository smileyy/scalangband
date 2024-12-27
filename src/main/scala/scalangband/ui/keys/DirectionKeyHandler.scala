package scalangband.ui.keys

import scalangband.model.Game
import scalangband.model.location.*
import scalangband.model.player.action.{CloseAction, OpenAction, PlayerAction}
import scalangband.ui.gamepanel.overlay.GamePanelOverlay

import scala.swing.event.{Key, KeyPressed}

class DirectionKeyHandler(factory: DirectionActionFactory) extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Key1, _, _) => Left(Some(factory(DownLeftDirection)))
      case KeyPressed(_, Key.Key2, _, _) => Left(Some(factory(DownDirection)))
      case KeyPressed(_, Key.Key3, _, _) => Left(Some(factory(DownRightDirection)))
      case KeyPressed(_, Key.Key4, _, _) => Left(Some(factory(LeftDirection)))
      case KeyPressed(_, Key.Key6, _, _) => Left(Some(factory(RightDirection)))
      case KeyPressed(_, Key.Key7, _, _) => Left(Some(factory(UpLeftDirection)))
      case KeyPressed(_, Key.Key8, _, _) => Left(Some(factory(UpDirection)))
      case KeyPressed(_, Key.Key9, _, _) => Left(Some(factory(UpRightDirection)))
      case KeyPressed(_, Key.Up, _, _) => Left(Some(factory(UpDirection)))
      case KeyPressed(_, Key.Down, _, _) => Left(Some(factory(DownDirection)))
      case KeyPressed(_, Key.Left, _, _) => Left(Some(factory(LeftDirection)))
      case KeyPressed(_, Key.Right, _, _) => Left(Some(factory(RightDirection)))
      case KeyPressed(_, _, _, _) => Left(None)
    }
  }
}

trait DirectionActionFactory {
  def apply(dir: Direction): PlayerAction
}

object CloseActionFactory extends DirectionActionFactory {
  override def apply(dir: Direction): PlayerAction = new CloseAction(dir)
}

object OpenActionFactory extends DirectionActionFactory {
  override def apply(dir: Direction): PlayerAction = new OpenAction(dir)
}
