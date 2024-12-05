package scalangband.model.action.player

import scalangband.model.Game
import scalangband.model.action.player.PlayerAction

trait PhysicalAction extends PlayerAction {
  override def energyRequired: Int = Game.BaseEnergyUnit
}
