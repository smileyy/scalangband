package scalangband.model.player

import scalangband.model.item.EquippableItem
import scalangband.model.item.armor.BodyArmor
import scalangband.model.item.weapon.Weapon

class Equipment(var weapon: Option[Weapon] = None, var body: Option[BodyArmor] = None) {
  
  def allEquipment: Seq[EquippableItem] = Seq(weapon, body).flatten
  override def toString: String = s"{ weapon: $weapon, body: $body }"
}