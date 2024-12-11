package scalangband.model.item.armor

import scalangband.model.item.EquippableItem

import scala.util.Random

class BodyArmor(val name: String, override val baseArmorClass: Int) extends Armor

trait BodyArmorFactory {
  def apply(random: Random, depth: Int): BodyArmor = {
    new BodyArmor(name, armorClass)
  }
  
  def name: String
  def armorClass: Int
}

