package scalangband.ui.gamepanel.overlay

import scalangband.model.Game
import scalangband.model.debug.{DebugLevelAction, FullHealAction}
import scalangband.model.player.action.PlayerAction
import scalangband.ui.keys.KeyHandler

import scala.swing.event.{Key, KeyPressed}

object WizardOverlay extends GamePanelOverlay {
  override def message: Option[String] = Some("Debug Command: ")
  override def keyHandler: KeyHandler = WizardKeyHandler
  override def paintable: Option[OverlayPanel] = None
}

object WizardKeyHandler extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] = {
    event match {
      case KeyPressed(_, Key.Escape, _, _) => Left(None)

      case KeyPressed(_, Key.A, _, _) => Left(Some(FullHealAction))
      case KeyPressed(_, Key.W, _, _) => Left(Some(DebugLevelAction))

      case _ => Left(None)
    }
  }
}