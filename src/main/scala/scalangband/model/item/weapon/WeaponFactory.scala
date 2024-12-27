package scalangband.model.item.weapon

import scalangband.model.item.*

import scala.util.Random

trait WeaponFactory extends ItemFactory {
  override def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): Weapon = {
    quality match {
      case NormalQuality => new Weapon(spec)
      case GoodQuality | GreatQuality =>
        val toHit = spec.damage.roll()
        val toDamage = spec.damage.roll()
        new Weapon(spec, toHit, toDamage)
    }
  }

  def spec: WeaponSpec
  def archetype: ItemArchetype = spec.archetype
}
