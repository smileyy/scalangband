package scalangband.model.monster

import scalangband.model.{Creature, Game}
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.GameAction
import scalangband.model.level.Level

import scala.util.Random

abstract class Monster(name: String, energy: Int = Random.nextInt(BaseEnergyUnit - 1) + 1) extends Creature(name, energy) {
  def speed: Int = BaseEnergyUnit

  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(level: Level): GameAction 
}