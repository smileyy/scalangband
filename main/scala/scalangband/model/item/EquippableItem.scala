package scalangband.model.item

trait EquippableItem extends Item {
  def baseArmorClass: Int = 0
  def toArmor: Int = 0

  def toHit: Int = 0
  def toDamage: Int = 0
  
  def onNextTurn(): Unit = {}
}
