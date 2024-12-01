package scalangband.model

import scalangband.model.Game.BaseEnergyUnit

class Player(val name: String, var energy: Int = Game.BaseEnergyUnit) extends Creature {
  override def regenerateEnergy(): Unit = energy = energy + speed
  
  def speed: Int = BaseEnergyUnit
}
