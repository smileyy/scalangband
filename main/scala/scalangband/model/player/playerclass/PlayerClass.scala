package scalangband.model.player.playerclass

import scalangband.data.item.weapon.Dagger
import scalangband.model.player.{Equipment, Inventory, StatBonuses, Stats}
import scalangband.model.util.DiceRoll

import scala.util.Random

trait PlayerClass {
  def name: String

  def startingStats: Stats
  def statBonus: StatBonuses
  
  def hitdice: DiceRoll
  
  def meleeSkill(level: Int): Int
  def savingThrow(level: Int): Int

  def startingEquipment(random: Random): Equipment
  def startingInventory(random: Random): Inventory
}
