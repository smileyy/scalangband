package scalangband.model

import scalangband.model.Game.BaseEnergyUnit

class Player(name: String, energy: Int = Game.BaseEnergyUnit) extends Creature(name, energy) {
  def speed: Int = BaseEnergyUnit

  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }
}
