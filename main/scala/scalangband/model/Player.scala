package scalangband.model

import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster

class Player(name: String, var coordinates: Coordinates, energy: Int = Game.BaseEnergyUnit) extends Creature(name, energy) {
  def speed: Int = BaseEnergyUnit

  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def attack(monster: Monster, callback: GameCallback): ActionResult = {
    var messages = List.empty[String]

    monster.health = monster.health - 1
    messages = s"You hit the ${monster.name}" :: messages
    if (monster.health <= 0) {
      callback.killMonster(monster)
      messages = s"The ${monster.name} dies" :: messages
    }

    new MessageResult(messages.reverse)
  }
}
