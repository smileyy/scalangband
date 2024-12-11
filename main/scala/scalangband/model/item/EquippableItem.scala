package scalangband.model.item

trait EquippableItem extends Item {
  def armorClass: Int = toArmor
  def toArmor: Int = 0

  def toHit: Int = 0
  
  def onNextTurn(): Unit = {}
}
