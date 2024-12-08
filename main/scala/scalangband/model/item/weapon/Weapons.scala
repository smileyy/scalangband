package scalangband.model.item.weapon

import scalangband.model.item.Item
import scalangband.model.util.DiceRoll

trait Weapon extends Item {
  def damage: DiceRoll
}

object Fists extends Weapon {
  override def name: String = "Fist"

  override def displayName: String = "your fists"

  override def damage: DiceRoll = DiceRoll("1d1")
}
