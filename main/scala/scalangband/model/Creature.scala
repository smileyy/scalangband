package scalangband.model

import scalangband.model.Representable

trait Creature extends Representable, Ordered[Creature] {
  def energy: Int
  def regenerateEnergy(): Unit

  override def compare(that: Creature): Int = energy - that.energy
}
