package scalangband.model.action

/**
 * An action that only involves user interface interaction (loo
 */
trait InterfaceAction extends GameAction {
  override def energyRequired: Int = 0
}
