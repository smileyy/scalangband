package scalangband.model.monster

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.Creature.NormalSpeed
import scalangband.model.item.{Armory, Item}
import scalangband.model.location.Coordinates
import scalangband.model.monster.action.MonsterAction
import scalangband.model.{Creature, GameAccessor}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.swing.Color
import scala.util.Random

class Monster(val spec: MonsterSpec, coordinates: Coordinates, var health: Int, var awake: Boolean, val inventory: mutable.ListBuffer[Item] = ListBuffer.empty) extends Creature(spec.name, coordinates, Monster.startingEnergy()) {
  def archetype: MonsterArchetype = spec.archetype
  def level: Int = spec.depth
  def experience: Int = spec.experience * level

  def speed: Int = spec.speed
  def armorClass: Int = spec.armorClass

  def alive: Boolean = spec.alive
  def invisible: Boolean = spec.invisible
  def clear: Boolean = spec.clear
  
  def addItem(item: Item): Unit = {
    inventory += item
  }

  /**
   * Anything that happens before a monster's next action.
   */
  def beforeNextAction(): List[ActionResult] = List.empty

  override def onNextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(game: GameAccessor): MonsterAction = spec.actions.select(this, game)

  def color: Color = spec.color

  def displayName: String = name.toLowerCase

  override def toString: String = {
    s"$name($health, ${inventory.mkString("[", ",", "]")})"
  }
}
object Monster {
  def apply(random: Random, spec: MonsterSpec, coordinates: Coordinates, armory: Armory): Monster = {
    val awake = random.nextInt(256) > spec.sleepiness
    val inventory = mutable.ListBuffer.from(spec.generateStartingInventory(random, armory))
    new Monster(spec, coordinates, spec.health.roll(), awake, inventory)
  }

  /**
   * All monsters start with some energy,  less than the player enters a level with.
   */
  def startingEnergy(): Int = Random.nextInt(NormalSpeed - 1) + 1
}