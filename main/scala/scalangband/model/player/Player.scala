package scalangband.model.player

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.{ActionResult, MessagesResult}
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.item.weapon.{Fists, Weapon}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.{Creature, Game, GameCallback}

import scala.util.Random

class Player(name: String, coordinates: Coordinates, var health: Int, energy: Int = Game.BaseEnergyUnit, skills: Skills, var money: Int, val inventory: Inventory, val equipment: Equipment)
  extends Creature(name, coordinates, energy) {

  def speed: Int = BaseEnergyUnit

  def light: Int = 3
  def weapon: Weapon = equipment.weapon.getOrElse(Fists)

  def toHit: Int = 24
  def armorClass: Int = equipment.allEquipment.map(_.armorClass).sum

  override def startNextTurn(): Unit = {
    regenerateEnergy()
  }

  def attack(monster: Monster, callback: GameCallback): ActionResult = {
    val messages = Random.nextInt(20) + 1 match {
      case 1 => handleMiss(monster)
      case 20 => handleHit(monster, callback)
      case _ => if (Random.nextInt(toHit) > monster.armorClass * 2 / 3) {
        handleHit(monster, callback)
      } else {
        handleMiss(monster)
      }
    }

    MessagesResult(messages)
  }

  private def handleHit(monster: Monster, callback: GameCallback): List[String] = {
    var messages: List[String] = List.empty

    val damage = weapon.damage.roll()
    monster.health = monster.health - damage

    messages = s"You hit the ${monster.displayName}." :: messages
    Player.Logger.info(s"Player hit ${monster.displayName} for $damage damage")
    if (monster.health <= 0) {
      callback.killMonster(monster)
      Player.Logger.info(s"Player killed ${monster.displayName}")
      messages = s"You have slain the ${monster.displayName}." :: messages
    }

    messages.reverse
  }

  private def handleMiss(monster: Monster): List[String] = {
    Player.Logger.info(s"Player killed ${monster.displayName}")
    List(s"You miss the ${monster.displayName}.")
  }
}
object Player {
  private val Logger = LoggerFactory.getLogger(classOf[Player])
}

class PlayerAccessor(private val player: Player) {
  def coordinates: Coordinates = player.coordinates
  def armorClass: Int = player.armorClass
}

class PlayerCallback(private val player: Player) {
  def attack(monster: Monster, callback: GameCallback): ActionResult = player.attack(monster, callback)
  def resetEnergy(): Unit = player.energy = player.speed

  def addMoney(amount: Int): Unit = player.money = player.money + amount
  
  def takeHit(damage: Int): Unit = player.health = player.health - damage
  
  def logInventory(): Unit = PlayerCallback.Logger.info(s"Inventory: ${player.inventory}")
  def logEquipment(): Unit = PlayerCallback.Logger.info(s"Equipment: ${player.equipment}")
}
object PlayerCallback {
  private val Logger = LoggerFactory.getLogger(classOf[PlayerCallback])
}