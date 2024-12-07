package scalangband.model.monster

import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.monster.MonsterAction
import scalangband.model.action.player.PlayerAction
import scalangband.model.item.Item
import scalangband.model.level.Level
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster.startingEnergy
import scalangband.model.util.Weighted
import scalangband.model.{Creature, Game, GameAccessor}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.swing.Color
import scala.util.Random

class Monster(val spec: MonsterSpec, coordinates: Coordinates, var health: Int, val inventory: mutable.ListBuffer[Item] = ListBuffer.empty) extends Creature(spec.name, coordinates, Monster.startingEnergy()) {
  def archetype: MonsterArchetype = spec.archetype

  def speed: Int = spec.baseSpeed

  def addItem(item: Item): Unit = {
    inventory += item
  }
  
  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(game: GameAccessor): MonsterAction = Weighted.selectFrom(spec.actions)
  
  def color: Color = spec.color
}
object Monster {
  def apply(spec: MonsterSpec, coordinates: Coordinates): Monster = {
    new Monster(spec, coordinates, spec.health.roll, mutable.ListBuffer.from(spec.generateStartingInventory()))
  }

  /**
   * All monsters start with some energy, but always less than the player enters a level with.
   */
  def startingEnergy(): Int = Random.nextInt(BaseEnergyUnit - 1) + 1
}