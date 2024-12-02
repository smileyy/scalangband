package scalangband.model

import scalangband.model.location.Coordinates

abstract class Creature(val name: String, var coordinates: Coordinates, var energy: Int) extends Representable, Ordered[Creature] {
  def speed: Int
  def deductEnergy(deduction: Int): Unit = energy = energy - deduction
  def regenerateEnergy(): Unit = energy = energy + speed
  
  def startNextTurn(): Unit
  
  override def compare(that: Creature): Int = energy - that.energy
}