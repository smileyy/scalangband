package scalangband.model.player.playerclass

import scalangband.data.item.armor.body.SoftLeatherArmor
import scalangband.data.item.weapon.Dagger
import scalangband.model.player.{Equipment, Inventory}
import scalangband.model.util.DiceRoll

import scala.util.Random

object Warrior extends PlayerClass {
  override val name: String = "Warrior"
  override val hitdice: DiceRoll = DiceRoll("1d10")

  override def meleeSkill(level: Int): Int = 70 + 5 * level
  override def savingThrow(level: Int): Int = 18 + level

  override def startingEquipment(random: Random): Equipment = new Equipment(
    weapon = Some(Dagger(random, 0)),
    body = Some(SoftLeatherArmor(random, 0))
  )

  override def startingInventory(random: Random): Inventory = Inventory.empty()
}
