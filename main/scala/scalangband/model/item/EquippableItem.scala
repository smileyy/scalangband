package scalangband.model.item

trait EquippableItem extends Item {
  def toArmor: Int = 0
  def armorClass: Int = toArmor
}
