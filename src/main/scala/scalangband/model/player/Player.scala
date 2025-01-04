package scalangband.model.player

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.{ActionResult, DeathResult, MessageResult}
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.effect.{Effect, EffectType, Fear, Paralysis}
import scalangband.model.element.Element
import scalangband.model.item.armor.{Armor, BodyArmor}
import scalangband.model.item.food.Food
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.potion.Potion
import scalangband.model.item.weapon.Weapon
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.player.playerclass.PlayerClass
import scalangband.model.player.race.Race
import scalangband.model.util.DiceRoll
import scalangband.model.{Creature, Game, GameCallback}

import scala.util.Random

class Player(
    name: String,
    val race: Race,
    val cls: PlayerClass,
    var baseStats: Stats,
    var health: Int,
    var satiety: Int,
    var experience: Experience,
    var levels: List[PlayerLevel],
    var money: Int,
    val inventory: Inventory,
    val equipment: Equipment,
    val effects: Effects,
    energy: Int,
    coords: Coordinates
) extends Creature(name, coords, energy) {

  val accessor: PlayerAccessor = new PlayerAccessor(this)
  val callback: PlayerCallback = new PlayerCallback(this)

  def stats: Stats = baseStats + (race.statBonus + cls.statBonus + equipment.statBonus)
  def level: Int = levels.size

  def maxHealth: Int = levels.map(_.hitpoints).sum + (stats.toHp * level).toInt
  def healthPercent: Int = (health * 100) / maxHealth
  def isDead: Boolean = health < 0

  def toHit: Int = cls.meleeSkill(level) + 3 * (equipment.toHit + stats.toHit)
  def toDamage: Int = equipment.toDamage + stats.toDamage
  def armorClass: Int = equipment.totalArmor + stats.toArmor

  def speed: Int = BaseEnergyUnit

  def lightRadius: Int = equipment.light.map(_.radius).getOrElse(0)

  private def savingThrow: Int = cls.savingThrow(level) + 3 * equipment.allEquipment.map(_.toHit).sum

  def canSeeInvisible = false

  def beforeNextAction(): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    results = effects.beforeNextAction(callback) ::: results
    if (isDead) {
      results = DeathResult() :: results
    }

    results = reduceSatiety() ::: results

    results
  }

  override def onNextTurn(): Unit = {
    regenerateEnergy()
    equipment.allEquipment.foreach(item => item.onNextTurn())
  }

  def addToInventory(item: Item): Unit = {
    inventory.addItem(item)
  }

  def removeFromInventory(item: Item): Unit = {
    inventory.removeItem(item)
  }

  def equip(item: EquippableItem): Option[EquippableItem] = {
    equipment.equip(item)
  }
  
  def unequip(unequipMethod: Equipment => Option[EquippableItem]): Option[EquippableItem] = {
    unequipMethod(equipment)
  }
  
  def addExperience(xp: Int): List[ActionResult] = {
    experience = experience + xp / level
    val newLevel = ExperienceTable.getLevel(race, experience.current)
    if (newLevel > level) {
      var results: List[ActionResult] = List.empty
      (level + 1 to newLevel).foreach { i =>
        val newLevelActual: PlayerLevel = PlayerLevel.next(race, cls)
        levels = newLevelActual :: levels
        results = MessageResult(s"Welcome to level $i.") :: results
      }

      results
    } else {
      List.empty
    }
  }
  
  def takeDamage(damage: Int, maybeElement: Option[Element], maybeEffect: Option[Effect]): List[ActionResult] = {
    Player.Logger.info(s"Player took $damage damage of element $maybeEffect and effect $maybeEffect")
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
        results = tryToAddEffect(eff) :: results
      }
    }

    results
  }

  def tryToAddEffect(effect: Effect): ActionResult = {
    if (Random.nextInt(100) > savingThrow) {
      effects.addEffect(effect)
    } else {
      MessageResult("You resist.")
    }
  }

  def reduceEffect(effectType: EffectType, amount: Int): ActionResult = {
    effects.reduceEffect(effectType, amount)
  }

  def quaff(potion: Potion, fromInventory: Boolean = true): List[ActionResult] = {
    val results = potion.onQuaff(callback)

    if (fromInventory) {
      inventory.removeItem(potion)
    }

    results
  }

  def eat(food: Food, fromInventory: Boolean = true): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    satiety = food.satiety.whenEaten(satiety)
    results = MessageResult(food.message) :: results

    if (fromInventory) {
      inventory.removeItem(food)
    }

    results
  }

  def reduceSatiety(amount: Int = 1): List[ActionResult] = {
    satiety = if (satiety - amount < 0) 0 else satiety - amount
    List.empty
  }

  def incapacitated: Boolean = {
    hasEffect(Paralysis)
  }

  def hasEffect(effectType: EffectType): Boolean = {
    effects.hasEffect(effectType)
  }

  private def resists(element: Element): Boolean = false

  def heal(amount: Int): ActionResult = {
    if (health + amount > maxHealth) {
      health = maxHealth
    } else {
      health = health + amount
    }

    MessageResult("You feel better.")
  }

  def fullHeal(): List[ActionResult] = {
    health = maxHealth
    List(MessageResult("You feel *much* better."))
  }
}
object Player {
  private val Logger = LoggerFactory.getLogger(classOf[Player])

  def newPlayer(random: Random, name: String, race: Race, cls: PlayerClass): Player = {
    val startingStats = cls.startingStats
    val levelOne = PlayerLevel.first(race: Race, cls: PlayerClass)

    new Player(
      name = name,
      race = race,
      cls = cls,
      baseStats = startingStats,
      health = levelOne.hitpoints + (startingStats + cls.statBonus).toHp.toInt,
      satiety = 4500,
      experience = Experience.None,
      levels = List(levelOne),
      money = DiceRoll("1d50+100").roll(),
      inventory = cls.startingInventory(random),
      equipment = cls.startingEquipment(random),
      effects = Effects.none(),
      energy = Game.BaseEnergyUnit,
      coords = Coordinates.Placeholder
    )
  }

  val MaxSatiety: Int = 5000
}

class PlayerAccessor(private val player: Player) {
  def coordinates: Coordinates = player.coordinates
  def armorClass: Int = player.armorClass
  def toHit: Int = player.toHit
  def toDamage: Int = player.toDamage
  def canSeeInvisible: Boolean = player.canSeeInvisible
  def hasEffect(effectType: EffectType): Boolean = player.hasEffect(effectType)
  def lightRadius: Int = player.lightRadius
  def satiety: Int = player.satiety
  def equipment: EquipmentAccessor = player.equipment.accessor
}

class PlayerCallback(private val player: Player) {
  def resetEnergy(): Unit = player.energy = player.speed

  def addExperience(xp: Int): List[ActionResult] = player.addExperience(xp)
  
  def takeDamage(damage: Int, element: Option[Element] = None, effect: Option[Effect] = None): List[ActionResult] = {
    player.takeDamage(damage, element, effect)
  }

  def tryToAddEffect(effect: Effect): ActionResult = player.tryToAddEffect(effect)
  def reduceEffect(effect: EffectType, turns: Int): ActionResult = player.reduceEffect(effect, turns)

  def addMoney(amount: Int): Unit = player.money = player.money + amount

  def addToInventory(item: Item): Unit = player.addToInventory(item)
  def removeFromInventory(item: Item): Unit = player.removeFromInventory(item)

  def equip(item: EquippableItem): Option[EquippableItem] = player.equip(item)
  def unequip(f: Equipment => Option[EquippableItem]): Option[EquippableItem] = player.unequip(f)

  def eat(food: Food): List[ActionResult] = player.eat(food)
  def quaff(potion: Potion, fromInventory: Boolean): List[ActionResult] = player.quaff(potion, fromInventory)

  def heal(amount: Int): ActionResult = player.heal(amount)
  def fullHeal(): List[ActionResult] = player.fullHeal()

  def logEquipment(): Unit = PlayerCallback.Logger.info(s"Equipment: ${player.equipment}")
}
object PlayerCallback {
  private val Logger = LoggerFactory.getLogger(classOf[PlayerCallback])
}