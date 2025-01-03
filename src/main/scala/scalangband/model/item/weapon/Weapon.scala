package scalangband.model.item.weapon

import scalangband.model.item.{EquippableItem, ItemArchetype}
import scalangband.model.util.DiceRoll

import scala.swing.Color

class Weapon(
    val spec: WeaponSpec,
    var weaponToHit: Int = 0,
    var weaponToDam: Int = 0
) extends EquippableItem {
  
  def name: String = spec.name
  def archetype: ItemArchetype = spec.archetype
  def damage: DiceRoll = spec.damage
  def color: Color = spec.color
  
  override def singular: String = {
    val toHitString = if (weaponToHit >= 0) s"+$weaponToHit" else weaponToHit.toString
    val toDamString = if (weaponToDam >= 0) s"+$weaponToDam" else weaponToDam.toString
    s"${spec.singular} ($damage) ($toHitString,$toDamString)"
  }
  
  override def toHit: Int = weaponToHit
  override def toDamage: Int = weaponToDam
}


