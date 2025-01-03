package scalangband.model.item.armor

import scalangband.model.item.EquippableItem

trait Armor extends EquippableItem {
  override def singular: String = {
    val toArmorString = if (toArmorBonus >= 0) s"+$toArmorBonus" else toArmorBonus.toString
    s"$name [$armorClass,$toArmorString]"
  }
}
