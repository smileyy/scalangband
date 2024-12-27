package scalangband.model.item.armor

import scalangband.model.item.*

import scala.swing.Color
import scala.util.Random

class BodyArmor(spec: ArmorSpec, var toArmor: Int = 0) extends Armor {
  override def name: String = spec.name
  override def archetype: ItemArchetype = spec.archetype
  override def color: Color = spec.color

  override def armorClass: Int = spec.armorClass

  override def toArmorBonus: Int = toArmor
}

trait BodyArmorFactory extends ItemFactory {
  def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): BodyArmor = quality match {
    case NormalQuality => new BodyArmor(spec)
    case GoodQuality | GreatQuality =>
      new BodyArmor(spec, Random.nextInt(spec.armorClass) + 1)
    case Artifact =>
      throw new Exception("You lucky dog!")
  }
  
  def spec: ArmorSpec
  override def archetype: ItemArchetype = spec.archetype
}
