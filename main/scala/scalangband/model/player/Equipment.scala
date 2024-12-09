package scalangband.model.player

import scalangband.data.item.weapon.Weapon
import scalangband.model.item.EquippableItem

class Equipment(var weapon: Option[Weapon] = None) {
  
  def allEquipment: Seq[EquippableItem] = Seq(weapon).flatten
  override def toString: String = s"{ weapoon: $weapon }"
}