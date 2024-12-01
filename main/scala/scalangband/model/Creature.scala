package scalangband.model

trait Creature extends Representable, Ordered[Creature] {
  def name: String
  def energy: Int
  def regenerateEnergy(): Unit

  override def compare(that: Creature): Int = energy - that.energy
}