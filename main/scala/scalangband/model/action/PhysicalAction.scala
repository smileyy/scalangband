package scalangband.model.action

import scalangband.model.Game

trait PhysicalAction extends GameAction {
  override def energyRequired: Int = Game.BaseEnergyUnit
}
