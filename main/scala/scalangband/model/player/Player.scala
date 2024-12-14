package scalangband.model.player

import org.slf4j.LoggerFactory
import scalangband.model.element.Element
import scalangband.model.Health
import scalangband.model.player.race.Race
import scalangband.model.player.playerclass.PlayerClass
import scalangband.bridge.actionresult.{ActionResult, DeathResult, MessageResult}
import scalangband.data.item.weapon.Fists
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.effect.{Effect, EffectType}
import scalangband.model.item.weapon.Weapon
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.util.DiceRoll
import scalangband.model.{Creature, Game, GameCallback}

import scala.util.Random

class Player(
    name: String,
    val race: Race,
    val cls: PlayerClass,
    var baseStats: Stats,
    var health: Int,
    var experience: Experience,
    var levels: List[PlayerLevel],
    var money: Int,
    val inventory: Inventory,
    val equipment: Equipment,
    val effects: Effects,
    energy: Int,
    coordinates: Coordinates
) extends Creature(name, coordinates, energy) {

  val accessor: PlayerAccessor = new PlayerAccessor(this)
  val callback: PlayerCallback = new PlayerCallback(this)

  def stats: Stats = baseStats + (race.statBonus + cls.statBonus + equipment.statBonus)
  def level: Int = levels.size
  def maxHealth: Int = levels.map(_.hitpoints).sum
  def healthPercent: Int = (health * 100) / maxHealth

  def toHit: Int = cls.meleeSkill(level) + 3 * (equipment.toHit + stats.toHit)
  def toDamage: Int = equipment.toDamage + stats.toDamage
  def armorClass: Int = equipment.totalArmor + stats.toArmor

  def speed: Int = BaseEnergyUnit

  def lightRadius: Int = equipment.light.map(_.radius).getOrElse(0)
  def weapon: Weapon = equipment.weapon.getOrElse(Fists)

  private def savingThrow: Int = cls.savingThrow(level) + 3 * equipment.allEquipment.map(_.toHit).sum

  def beforeNextAction(): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    results = effects.onNewTurn(callback) ::: results
    if (isDead) {
      results = DeathResult() :: results
    }

    results
  }

  override def onNextTurn(): Unit = {
    regenerateEnergy()
    equipment.allEquipment.foreach(item => item.onNextTurn())
  }

  private def addExperience(xp: Int): List[ActionResult] = {
    experience = experience + xp / level
    val newLevel = ExperienceTable.getLevel(race, experience.current)
    if (newLevel > level) {
      var results: List[ActionResult] = List.empty
      (level + 1 to newLevel).foreach { i =>
        val newLevelActual: PlayerLevel = PlayerLevel.next(race, cls)
        levels = newLevelActual :: levels
        results = MessageResult(s"Welcome to level $i") :: results
      }

      results
    } else {
      List.empty
    }
  }


  def attack(monster: Monster, callback: GameCallback): List[ActionResult] = {
    Random.nextInt(20) + 1 match {
      case 1  => handleMiss(monster)
      case 20 => handleHit(monster, callback)
      case _ =>
        if (Random.nextInt(toHit) > monster.armorClass * 3 / 4) {
          handleHit(monster, callback)
        } else {
          handleMiss(monster)
        }
    }
  }

  private def handleHit(monster: Monster, callback: GameCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    val damage = Math.max(weapon.damage.roll() + toDamage, 0)
    monster.health = monster.health - damage
    monster.awake = true

    results = MessageResult(s"You hit the ${monster.displayName}.") :: results
    Player.Logger.info(s"Player hit ${monster.displayName} for $damage damage")
    if (monster.health <= 0) {
      callback.killMonster(monster)
      Player.Logger.info(s"Player killed ${monster.displayName}")
      results = MessageResult(s"You have slain the ${monster.displayName}.") :: results

      results = addExperience(monster.experience) ::: results
    }

    results
  }

  private def handleMiss(monster: Monster): List[ActionResult] = {
    Player.Logger.info(s"Player killed ${monster.displayName}")
    List(MessageResult(s"You miss the ${monster.displayName}."))
  }

  def takeDamage(damage: Int, maybeElement: Option[Element], maybeEffect: Option[Effect]): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    val actualDamage = maybeElement match {
      case Some(element) if resists(element) => damage / 2
      case _                                 => damage
    }

    maybeElement match {
      case Some(element) =>
        element.message match {
          case Some(message) => results = MessageResult(message) :: results
          case None          =>
        }
      case None =>
    }

    health = health - actualDamage
    if (isDead) {
      results = DeathResult() :: results
    } else {
      maybeEffect.foreach { eff =>
        if (Random.nextInt(100) > savingThrow) {
          results = effects.addEffect(eff) :: results
        } else {
          results = MessageResult("You resist.") :: results
        }
      }
    }

    results
  }

  def hasEffect(effectType: EffectType): Boolean = {
    effects.hasEffect(effectType)
  }

  private def resists(element: Element): Boolean = false

  def isDead: Boolean = health < 0
}
object Player {
  private val Logger = LoggerFactory.getLogger(classOf[Player])

  def newPlayer(random: Random, name: String, race: Race, cls: PlayerClass): Player = {
    val levelOne = PlayerLevel.first(race: Race, cls: PlayerClass)

    new Player(
      name = name,
      race = race,
      cls = cls,
      baseStats = cls.startingStats,
      health = levelOne.hitpoints,
      experience = Experience.None,
      levels = List(levelOne),
      money = DiceRoll("1d50+100").roll(),
      inventory = cls.startingInventory(random),
      equipment = cls.startingEquipment(random),
      effects = Effects.none(),
      energy = Game.BaseEnergyUnit,
      coordinates = Coordinates.Placeholder
    )
  }
}

class PlayerAccessor(private val player: Player) {
  def coordinates: Coordinates = player.coordinates
  def armorClass: Int = player.armorClass
  def hasEffect(effectType: EffectType): Boolean = player.hasEffect(effectType)
}

class PlayerCallback(private val player: Player) {
  def attack(monster: Monster, callback: GameCallback): List[ActionResult] = player.attack(monster, callback)
  def resetEnergy(): Unit = player.energy = player.speed

  def takeDamage(damage: Int, element: Option[Element], effect: Option[Effect]): List[ActionResult] = {
    player.takeDamage(damage, element, effect)
  }

  def addMoney(amount: Int): Unit = player.money = player.money + amount

  def logInventory(): Unit = PlayerCallback.Logger.info(s"Inventory: ${player.inventory}")
  def logEquipment(): Unit = PlayerCallback.Logger.info(s"Equipment: ${player.equipment}")
}
object PlayerCallback {
  private val Logger = LoggerFactory.getLogger(classOf[PlayerCallback])
}
