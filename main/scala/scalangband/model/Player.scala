package scalangband.model

import scalangband.model.Game.BaseEnergyUnit

class Player(val name: String, var energy: Int = 100) extends Creature {
  override def regenerateEnergy(): Unit = energy = energy + speed
  
  def speed: Int = BaseEnergyUnit
}
