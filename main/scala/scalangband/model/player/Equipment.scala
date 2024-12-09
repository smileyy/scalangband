package scalangband.model.player

import scalangband.model.item.EquippableItem
import scalangband.model.item.weapon.Weapon

class Equipment(var weapon: Option[Weapon] = None) {
  
  def allEquipment: Seq[EquippableItem] = Seq(weapon).flatten
  override def toString: String = s"{ weapoon: $weapon }"
}