package scalangband.model.action.player

import scalangband.model.action.player.PlayerAction

/**
 * An action that only involves user interface interaction (loo
 */
trait InterfaceAction extends PlayerAction {
  override def energyRequired: Int = 0
}
