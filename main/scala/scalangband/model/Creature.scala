package scalangband.model

import scalangband.model.location.Coordinates

abstract class Creature(val name: String, var coordinates: Coordinates, var energy: Int) extends Representable {
  def speed: Int
  def deductEnergy(deduction: Int): Unit = energy = energy - deduction
  def regenerateEnergy(): Unit = energy = energy + speed
  
  def startNextTurn(): Unit

  override def toString: String = s"$name($energy)"
}
object Creature {
  val NormalSpeed = 20
}
