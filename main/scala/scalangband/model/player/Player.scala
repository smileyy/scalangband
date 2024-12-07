package scalangband.model.player

import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.{Creature, Game, GameCallback}

class Player(name: String, coordinates: Coordinates, energy: Int = Game.BaseEnergyUnit, val inventory: Inventory) extends Creature(name, coordinates, energy) {
  def speed: Int = BaseEnergyUnit

  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def attack(monster: Monster, callback: GameCallback): ActionResult = {
    var messages = List.empty[String]

    monster.health = monster.health - 1
    messages = s"You hit the ${monster.name}." :: messages
    if (monster.health <= 0) {
      callback.killMonster(monster)
      messages = s"The ${monster.name} dies." :: messages
    }

    MessagesResult(messages.reverse)
  }
}

class PlayerAccessor(private val player: Player) {
  def coordinates: Coordinates = player.coordinates
}

class PlayerCallback(private val player: Player) {
  def attack(monster: Monster, callback: GameCallback): ActionResult = player.attack(monster, callback)
  def resetEnergy(): Unit = player.energy = player.speed

  def logInventory(): Unit = println(s"Inventory: ${player.inventory.items.map(_.name).mkString("(", ",", ")")}")
}