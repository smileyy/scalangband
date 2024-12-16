package scalangband.model.item.weapon

import scalangband.model.item.{ItemArchetype, ItemFactory, ItemQuality, NormalQuality}

import scala.util.Random

trait WeaponFactory extends ItemFactory {
  override def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): Weapon = {
    new Weapon(spec)
  }

  def spec: WeaponSpec
  def archetype: ItemArchetype = spec.archetype
}
