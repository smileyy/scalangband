package scalangband.model.monster

import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.GameAction
import scalangband.model.item.Item
import scalangband.model.level.Level
import scalangband.model.location.Coordinates
import scalangband.model.util.Weighted
import scalangband.model.{Creature, Game}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

abstract class Monster(name: String, coordinates: Coordinates, var health: Int, energy: Int = Random.nextInt(BaseEnergyUnit - 1) + 1) extends Creature(name, coordinates, energy) {
  val inventory: mutable.ListBuffer[Item] = ListBuffer.empty
  def speed: Int = BaseEnergyUnit

  def addItem(item: Item): Unit = {
    inventory += item
  }
  
  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(level: Level): GameAction = Weighted.select(actions)
  
  def actions: Seq[Weighted[GameAction]]
}