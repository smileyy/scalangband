package scalangband.model.player.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.{GameAccessor, GameCallback}

/**
 * An action that only involves user interface interaction (loo
 */
trait InterfaceAction extends PlayerAction {
  override def energyRequired: Int = 0
}