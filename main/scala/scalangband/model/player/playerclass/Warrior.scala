package scalangband.model.player.playerclass

import scalangband.data.item.armor.body.SoftLeatherArmor
import scalangband.data.item.food.RationOfFood
import scalangband.data.item.lightsource.WoodenTorch
import scalangband.data.item.weapon.Dagger
import scalangband.model.item.GoodQuality
import scalangband.model.player.{Equipment, Inventory, StatBonuses, Stats}
import scalangband.model.util.DiceRoll

import scala.util.Random

object Warrior extends PlayerClass {
  override val name: String = "Warrior"

  override def startingStats: Stats = Stats(str = 17, intg = 10, wis = 10, dex = 16, con = 16)
  override def statBonus: StatBonuses = StatBonuses(str = 3, intg = -2, wis = -2, dex = 2, con = 2)

  override val hitdice: DiceRoll = DiceRoll("1d10")

  override def meleeSkill(level: Int): Int = (70 + 4.5 * level).toInt
  override def savingThrow(level: Int): Int = 18 + level

  override def startingEquipment(random: Random): Equipment = new Equipment(
    weapon = Some(Dagger()),
    light = Some(WoodenTorch()),
    body = Some(SoftLeatherArmor())
  )

  override def startingInventory(random: Random): Inventory = {
    val inventory = Inventory.empty()
    (1 to DiceRoll("1d3").roll()).foreach(_ => inventory.addItem(RationOfFood()))
    inventory
  }
}
