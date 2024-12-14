package scalangband.model.item.armor

import scalangband.model.item.EquippableItem

trait Armor extends EquippableItem {
  override def displayName: String = s"$name [$armorClass, $toArmor]"
}
