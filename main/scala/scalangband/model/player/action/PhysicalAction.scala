package scalangband.model.player.action

import scalangband.model.Game

trait PhysicalAction extends PlayerAction {
  override def energyRequired: Int = Game.BaseEnergyUnit
}
