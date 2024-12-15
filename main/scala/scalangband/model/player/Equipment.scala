package scalangband.model.player

import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.item.armor.BodyArmor
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.weapon.Weapon
import scalangband.model.player.StatBonuses.NoBonus

class Equipment(
    var weapon: Option[Weapon] = None,
    var light: Option[LightSource] = None,
    var body: Option[BodyArmor] = None
) {

  def unwieldWeapon(): Option[Item] = {
    weapon match {
      case Some(w) => weapon = None; Some(w)
      case None => None
    }
  }
  
  def removeLight(): Option[Item] = {
    light match {
      case Some(l) => light = None; Some(l)
      case None => None
    }
  }
  
  def removeBodyArmor(): Option[Item] = {
    body match {
      case Some(b) => body = None; Some(b)
      case None => None
    }
  }
  
  def allEquipment: Seq[EquippableItem] = Seq(weapon, light, body).flatten

  def armorClass: Int = allEquipment.map(_.armorClass).sum
  def toArmor: Int = allEquipment.map(_.toArmor).sum
  def totalArmor: Int = armorClass + toArmor
  
  def toHit: Int = allEquipment.map(_.toHit).sum
  def toDamage: Int = allEquipment.map(_.toDamage).sum
  
  def statBonus: StatBonuses = allEquipment.map(_.statBonus).foldLeft(NoBonus)((acc, bonus) => acc + bonus)

  override def toString: String = s"{ weapon: $weapon, light: $light, body: $body }"
}
