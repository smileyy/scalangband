package scalangband.data.item.weapon

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.item.Sword
import scalangband.model.item.weapon.{WeaponFactory, WeaponSpec}
import scalangband.model.util.DiceRoll

object Dagger extends WeaponFactory {
  override val spec: WeaponSpec = WeaponSpec(
    name = "Dagger", singular = "a Dagger", archetype = Sword, damage = DiceRoll("1d4"), color = White
  )

  override val levels: Range = 1 to 100
  override val commonness: Int = 20
}

object MainGauche extends WeaponFactory {
  override val spec: WeaponSpec = WeaponSpec(
    name = "Main Gauche", singular = "a Main Gauche", archetype = Sword, damage = DiceRoll("1d5"), color = White
  )

  override def levels: Range = 3 to 100
  override def commonness: Int = 20
}