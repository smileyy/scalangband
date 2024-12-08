package scalangband.model.player

import org.slf4j.LoggerFactory
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.player.PlayerCallback.Logger
import scalangband.model.{Creature, Game, GameCallback}

import scala.util.Random

class Player(name: String, coordinates: Coordinates, energy: Int = Game.BaseEnergyUnit, skills: Skills, var money: Int, val inventory: Inventory, val equipment: Equipment) extends Creature(name, coordinates, energy) {
  def speed: Int = BaseEnergyUnit

  def light: Int = 3

  def meleeSkill: Int = skills.melee

  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def attack(monster: Monster, callback: GameCallback): ActionResult = {
    val messages = Random.nextInt(5) match {
      case 0 => handleMiss(monster)
      case 4 => handleHit(monster, callback)
      case _ => if (Random.nextInt(meleeSkill) > monster.evasion * 2 / 3) {
        handleHit(monster, callback)
      } else {
        handleMiss(monster)
      }
    }

    MessagesResult(messages)
  }

  private def handleHit(monster: Monster, callback: GameCallback): List[String] = {
    var messages: List[String] = List.empty

    val damage = equipment.getWeapon.damage.roll()
    monster.health = monster.health - damage

    messages = s"You hit the ${monster.displayName}." :: messages
    if (monster.health <= 0) {
      callback.killMonster(monster)
      messages = s"You have slain the ${monster.displayName}." :: messages
    }

    messages.reverse
  }

  private def handleMiss(monster: Monster): List[String] = {
    List(s"You miss the ${monster.displayName}.")
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