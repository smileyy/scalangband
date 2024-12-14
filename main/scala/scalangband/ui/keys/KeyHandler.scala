package scalangband.ui.keys

import scalangband.model.Game
import scalangband.model.player.action.PlayerAction
import scalangband.ui.gamepanel.overlay.GamePanelOverlay

import scala.swing.event.KeyPressed

trait KeyHandler {
  def handleKeyPressed(event: KeyPressed): Either[Option[PlayerAction], GamePanelOverlay]
}