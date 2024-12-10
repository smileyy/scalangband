package scalangband.model.player

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.{ActionResult, DeathResult, MessagesResult, NoResult}
import scalangband.data.item.weapon.{Fists, Weapon}
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.effect.{Effect, EffectType}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.{Creature, Game, GameCallback}

import scala.util.Random

class Player(name: String, coordinates: Coordinates, var health: Int, energy: Int = Game.BaseEnergyUnit, skills: Skills, var money: Int, val inventory: Inventory, val equipment: Equipment, val effects: Effects)
  extends Creature(name, coordinates, energy) {

  val callback: PlayerCallback = new PlayerCallback(this)

  def speed: Int = BaseEnergyUnit

  def light: Int = 3
  def weapon: Weapon = equipment.weapon.getOrElse(Fists)

  def toHit: Int = 24
  def armorClass: Int = equipment.allEquipment.map(_.armorClass).sum

  def beforeNextAction(): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    results = effects.onNewTurn(callback) ::: results
    if (health <= 0) {
      results = DeathResult() :: results
    }

    results
  }
  
  override def nextTurn(): Unit = {
    regenerateEnergy()
  }

  def attack(monster: Monster, callback: GameCallback): List[ActionResult] = {
    Random.nextInt(20) + 1 match {
      case 1 => handleMiss(monster)
      case 20 => handleHit(monster, callback)
      case _ => if (Random.nextInt(toHit) > monster.armorClass * 2 / 3) {
        handleHit(monster, callback)
      } else {
        handleMiss(monster)
      }
    }
  }

  private def handleHit(monster: Monster, callback: GameCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    val damage = weapon.damage.roll()
    monster.health = monster.health - damage

    results = MessagesResult(s"You hit the ${monster.displayName}.") :: results
    Player.Logger.info(s"Player hit ${monster.displayName} for $damage damage")
    if (monster.health <= 0) {
      callback.killMonster(monster)
      Player.Logger.info(s"Player killed ${monster.displayName}")
      results = MessagesResult(s"You have slain the ${monster.displayName}.") :: results
    }

    results
  }

  private def handleMiss(monster: Monster): List[ActionResult] = {
    Player.Logger.info(s"Player killed ${monster.displayName}")
    List(MessagesResult(s"You miss the ${monster.displayName}."))
  }

  def takeDamage(damage: Int, maybeEffect: Option[Effect]): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    health = health - damage
    if (health <= 0) {
      results = DeathResult() :: results
    } else {
      maybeEffect.foreach { effect =>
        results = effects.addEffect(effect) :: results
      }
    }

    results
  }
  
  def hasEffect(effectType: EffectType): Boolean = {
    effects.hasEffect(effectType)
  }

  def isDead: Boolean = health <= 0
}
object Player {
  private val Logger = LoggerFactory.getLogger(classOf[Player])
}

class PlayerAccessor(private val player: Player) {
  def coordinates: Coordinates = player.coordinates
  def armorClass: Int = player.armorClass
  def hasEffect(effectType: EffectType): Boolean = player.hasEffect(effectType)
}

class PlayerCallback(private val player: Player) {
  def attack(monster: Monster, callback: GameCallback): List[ActionResult] = player.attack(monster, callback)
  def resetEnergy(): Unit = player.energy = player.speed

  def takeDamage(damage: Int, maybeEffect: Option[Effect]): List[ActionResult] = {
    player.takeDamage(damage, maybeEffect)
  }

  def addMoney(amount: Int): Unit = player.money = player.money + amount
  
  def logInventory(): Unit = PlayerCallback.Logger.info(s"Inventory: ${player.inventory}")
  def logEquipment(): Unit = PlayerCallback.Logger.info(s"Equipment: ${player.equipment}")
}
object PlayerCallback {
  private val Logger = LoggerFactory.getLogger(classOf[PlayerCallback])
}