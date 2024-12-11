package scalangband.model.item.weapon

import scalangband.model.item.EquippableItem
import scalangband.model.util.DiceRoll

import scala.util.Random

class Weapon(val name: String, val damage: DiceRoll, var weaponToHit: Int = 0, var weaponToDam: Int = 0) extends EquippableItem {
  override def displayName: String = s"$name ($damage) ($weaponToHit,$weaponToDam)"
  override def toHit: Int = weaponToHit
  override def toDamage: Int = weaponToDam
}

trait WeaponFactory {
  def apply(random: Random, depth: Int): Weapon = {
    new Weapon(name, damage)
  }

  def name: String
  def damage: DiceRoll
}