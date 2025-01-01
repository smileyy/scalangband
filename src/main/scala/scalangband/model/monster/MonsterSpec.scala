package scalangband.model.monster

import scalangband.model.item.{Armory, Item}
import scalangband.model.monster.action.MonsterActions
import scalangband.model.util.DiceRoll
import scalangband.model.{Creature, Legendarium}

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
    val invisible: Boolean = false,
    val clear: Boolean = false,
    val breeds: Boolean = false,
    val doors: MonsterDoorStrategy = CantOpenDoors,
    val actions: MonsterActions,
    val inventory: Seq[MonsterInventoryGenerator] = Seq.empty,
    val friends: Seq[MonsterFriendSpec] = Seq.empty,
    val color: Color
) {
  def generateStartingInventory(random: Random, armory: Armory): Seq[Item] = {
    inventory.flatMap(generator => generator.generateItem(random, armory))
  }
}