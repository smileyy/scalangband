package scalangband.model.item.weapon

import scalangband.model.item.EquippableItem
import scalangband.model.util.DiceRoll

class Weapon(val name: String, val damage: DiceRoll, var toHit: Int = 0, var toDam: Int = 0) extends EquippableItem {
  override def displayName: String = s"$name ($damage) ($toHit,$toDam)"

  override def toString: String = displayName
}
