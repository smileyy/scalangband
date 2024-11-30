package scalangband.model.monster

import scalangband.model.Creature
import scalangband.model.Game.BaseEnergyUnit

import scala.util.Random

abstract class Monster(val name: String, var energy: Int = Random.nextInt(BaseEnergyUnit - 1) + 1) extends Creature {
  override def regenerateEnergy(): Unit = energy = energy + speed
  private def speed: Int = BaseEnergyUnit


  def unique: Boolean = false
}