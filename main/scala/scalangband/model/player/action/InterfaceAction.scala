package scalangband.model.player.action

/**
 * An action that only involves user interface interaction (loo
 */
trait InterfaceAction extends PlayerAction {
  override def energyRequired: Int = 0
}
