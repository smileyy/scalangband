package scalangband.model.item.armor

import scalangband.model.item.{ItemArchetype, ItemFactory, ItemQuality, NormalQuality}

import scala.swing.Color
import scala.util.Random

class BodyArmor(spec: ArmorSpec, var toArmor: Int = 0) extends Armor {
  override def name: String = spec.name
  override def archetype: ItemArchetype = spec.archetype
  override def color: Color = spec.color

  override def toArmorBonus: Int = toArmor
}

trait BodyArmorFactory extends ItemFactory {
  def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): BodyArmor = {
    new BodyArmor(spec)
  }
  
  def spec: ArmorSpec
  override def archetype: ItemArchetype = spec.archetype
}
