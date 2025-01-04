package scalangband.model.player

import scalangband.model.item.armor.BodyArmor
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.weapon.Weapon
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.player.StatBonuses.NoBonus

class Equipment(
    var weapon: Option[Weapon] = None,
    var light: Option[LightSource] = None,
    var body: Option[BodyArmor] = None
) {
  
  def wield(w: Weapon): Option[Weapon] = {
    val result = weapon
    weapon = Some(w)
    result
  }

  def unwieldWeapon(): Option[Weapon] = {
    weapon match {
      case Some(w) => weapon = None; Some(w)
      case None => None
    }
  }
  
  def wield(l: LightSource): Option[LightSource] = {
    val result = light
    light = Some(l)
    result
  }
  
  def removeLight(): Option[LightSource] = {
    light match {
      case Some(l) => light = None; Some(l)
      case None => None
    }
  }
  
  def wear(b: BodyArmor): Option[BodyArmor] = {
    val result = body
    body = Some(b)
    result
  }
  
  def removeBodyArmor(): Option[BodyArmor] = {
    body match {
      case Some(b) => body = None; Some(b)
      case None => None
    }
  }
  
  def allPossibleEquipment: Seq[Option[EquippableItem]] = Seq(weapon, light, body)
  def allEquipment: Seq[EquippableItem] = allPossibleEquipment.flatten

  def armorClass: Int = allEquipment.map(_.armorClass).sum
  def toArmor: Int = allEquipment.map(_.toArmorBonus).sum
  def totalArmor: Int = armorClass + toArmor
  
  def toHit: Int = allEquipment.map(_.toHit).sum
  def toDamage: Int = allEquipment.map(_.toDamage).sum
  
  def statBonus: StatBonuses = allEquipment.map(_.statBonus).foldLeft(NoBonus)((acc, bonus) => acc + bonus)

  override def toString: String = s"{ weapon: $weapon, light: $light, body: $body }"
}
