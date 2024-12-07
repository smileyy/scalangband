package scalangband.ui.keys

import scalangband.model.Game
import scalangband.model.action.player.PlayerAction
import scalangband.ui.GamePanelCallback

import scala.swing.event.KeyPressed

trait KeyHandler {
  def handleKeyPressed(event: KeyPressed, game: Game, callback: GamePanelCallback): Option[PlayerAction]
}
