package scalangband.model.monster

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.Creature.NormalSpeed
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.item.Item
import scalangband.model.location.Coordinates
import scalangband.model.monster.action.MonsterAction
import scalangband.model.util.Weighted
import scalangband.model.{Creature, Game, GameAccessor}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.swing.Color
import scala.util.Random

class Monster(val spec: MonsterSpec, coordinates: Coordinates, var health: Int, val inventory: mutable.ListBuffer[Item] = ListBuffer.empty) extends Creature(spec.name, coordinates, Monster.startingEnergy()) {
  def archetype: MonsterArchetype = spec.archetype
  def level: Int = spec.level
  
  def speed: Int = spec.speed
  def armorClass: Int = spec.armorClass

  def addItem(item: Item): Unit = {
    inventory += item
  }

  /**
   * Anything that happens before a monster's next action.
   */
  def beforeNextAction(): List[ActionResult] = List.empty
  
  override def nextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(game: GameAccessor): MonsterAction = Weighted.selectFrom(spec.actions)
  
  def color: Color = spec.color

  def displayName: String = name.toLowerCase
  
  override def toString: String = {
    s"$name($health, ${inventory.mkString("[", ",", "]")})"
  }
}
object Monster {
  def apply(spec: MonsterSpec, coordinates: Coordinates, random: Random): Monster = {
    new Monster(spec, coordinates, spec.health.roll(), mutable.ListBuffer.from(spec.generateStartingInventory(random)))
  }

  /**
   * All monsters start with some energy,  less than the player enters a level with.
   */
  def startingEnergy(): Int = Random.nextInt(NormalSpeed - 1) + 1
}