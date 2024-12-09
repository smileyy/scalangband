package scalangband.data.item.weapon

import scalangband.model.item.weapon.{Weapon, WeaponFactory}
import scalangband.model.util.DiceRoll

object Fists extends Weapon("Fists", DiceRoll("1d1")) {}

object Dagger extends WeaponFactory {
  override def name: String = "Dagger"
  override def damage: DiceRoll = DiceRoll("1d4")
}