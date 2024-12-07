package scalangband.model.player

import org.slf4j.LoggerFactory
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.player.PlayerCallback.Logger
import scalangband.model.{Creature, Game, GameCallback}

class Player(name: String, coordinates: Coordinates, energy: Int = Game.BaseEnergyUnit, var money: Int, val inventory: Inventory) extends Creature(name, coordinates, energy) {
  def speed: Int = BaseEnergyUnit

  def light: Int = 3

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

  def addMoney(amount: Int): Unit = {
    player.money = player.money + amount
  }
  def logInventory(): Unit = Logger.info(s"Inventory: ${player.inventory.items.map(_.name).mkString("(", ",", ")")}")
}
object PlayerCallback {
  private val Logger = LoggerFactory.getLogger(classOf[PlayerCallback])
}