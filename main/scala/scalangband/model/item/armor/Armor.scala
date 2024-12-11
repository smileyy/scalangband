package scalangband.model.item.armor

import scalangband.model.item.EquippableItem

trait Armor extends EquippableItem {
  def baseArmorClass: Int

  override def armorClass: Int = baseArmorClass + toArmor

  override def displayName: String = s"$name [$armorClass, $toArmor]"
}
