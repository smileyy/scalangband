package scalangband.model.monster

import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.GameAction
import scalangband.model.item.Item
import scalangband.model.level.Level
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster.startingEnergy
import scalangband.model.util.Weighted
import scalangband.model.{Creature, Game, GameAccessor}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

abstract class Monster(name: String, coordinates: Coordinates, energy: Int = startingEnergy(), var health: Int, val inventory: mutable.ListBuffer[Item] = ListBuffer.empty) extends Creature(name, coordinates, energy) {
  def speed: Int = BaseEnergyUnit

  def addItem(item: Item): Unit = {
    inventory += item
  }
  
  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(game: GameAccessor): GameAction = Weighted.selectFrom(actions)
  
  def actions: Seq[Weighted[GameAction]]
}
object Monster {
  /**
   * All monsters start with some energy, but always less than the player enters a level with.
   */
  def startingEnergy(): Int = Random.nextInt(BaseEnergyUnit - 1) + 1
}