package scalangband.model.player.action

trait FreeAction extends PlayerAction {
  override def energyRequired: Int = 0
}
