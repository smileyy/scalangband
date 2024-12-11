package scalangband.data.item.armor.body

import scalangband.model.item.armor.BodyArmorFactory

object SoftLeatherArmor extends BodyArmorFactory {
  override def name: String = "Soft Leather Armor"
  override def armorClass: Int = 8
}
