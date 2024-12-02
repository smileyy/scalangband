package scalangband.model.monster

import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.GameAction
import scalangband.model.level.Level
import scalangband.model.location.Coordinates
import scalangband.model.util.Weighted
import scalangband.model.{Creature, Game}

import scala.util.Random

abstract class Monster(name: String, var health: Int, energy: Int = Random.nextInt(BaseEnergyUnit - 1) + 1, coordinates: Coordinates) extends Creature(name, coordinates, energy) {
  def speed: Int = BaseEnergyUnit

  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(level: Level): GameAction = Weighted.select(weightedActions)
  
  def weightedActions: Seq[Weighted[GameAction]]
}