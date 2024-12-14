package scalangband.ui.keys

import scalangband.model.Game
import scalangband.model.player.action.PlayerAction
import scalangband.ui.gamepanel.GamePanelCallback

import scala.swing.event.KeyPressed

trait KeyHandler {
  def handleKeyPressed(event: KeyPressed, game: Game, callback: GamePanelCallback): Option[PlayerAction]
}
