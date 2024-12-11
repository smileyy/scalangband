package scalangband.model.item.armor

import scalangband.model.item.EquippableItem

import scala.util.Random

class BodyArmor(val name: String, override val baseArmorClass: Int, var armorToArmor: Int) extends Armor {
  override def toArmor: Int = armorToArmor
}

trait BodyArmorFactory {
  def apply(random: Random, depth: Int): BodyArmor = {
    new BodyArmor(name, baseArmorClass, toArmor)
  }
  
  def name: String
  def baseArmorClass: Int
  def toArmor: Int = 0
}

