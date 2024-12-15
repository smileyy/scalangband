package scalangband.model.monster

import scalangband.model.Creature
import scalangband.model.item.Item
import scalangband.model.monster.action.{MonsterAction, MonsterActions}
import scalangband.model.util.{DiceRoll, Weighted}

import scala.swing.Color
import scala.util.Random

/** The idea of this type is that it will allow monster recall to be generated from it while serving as a source of 
 * immutable monster data 
 */
class MonsterSpec(
    val name: String,
    val archetype: MonsterArchetype,
    val depth: Int,
    val health: DiceRoll,
    val speed: Int = Creature.NormalSpeed,
    val armorClass: Int,
    val experience: Int,
    val sleepiness: Int,
    val alive: Boolean = true,
    val actions: MonsterActions,
    val inventory: Seq[MonsterInventoryGenerator] = Seq.empty,
    val color: Color
) {
  def generateStartingInventory(random: Random): Seq[Item] = {
    inventory.flatMap(generator => generator.generate(random, depth))
  }
  
}
