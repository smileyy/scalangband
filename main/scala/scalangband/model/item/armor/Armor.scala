package scalangband.model.item.armor

import scalangband.model.item.EquippableItem

trait Armor extends EquippableItem {
  override def displayName: String = {
    val toArmorString = if (toArmor >= 0) s"+$toArmor" else toArmor.toString
    s"$name [$armorClass,$toArmorString]"
  }
}
