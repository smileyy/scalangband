package scalangband.model.monster

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.Creature.NormalSpeed
import scalangband.model.item.{Armory, Item}
import scalangband.model.location.Coordinates
import scalangband.model.monster.action.MonsterAction
import scalangband.model.tile.Floor
import scalangband.model.{Creature, GameAccessor, GameCallback}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.swing.Color
import scala.util.Random

class Monster(
    val factory: MonsterFactory,
    coordinates: Coordinates,
    var health: Int,
    var awake: Boolean,
    val inventory: mutable.ListBuffer[Item] = ListBuffer.empty
) extends Creature(factory.spec.name, coordinates, Monster.startingEnergy()) {
  private def spec = factory.spec

  def archetype: MonsterArchetype = spec.archetype
  def level: Int = spec.depth
  def experience: Int = spec.experience * level

  def speed: Int = spec.speed
  def hearing: Int = spec.hearing
  def armorClass: Int = spec.armorClass

  def alive: Boolean = spec.alive
  def invisible: Boolean = spec.invisible
  def clear: Boolean = spec.clear
  def breeds: Boolean = spec.breeds
  def doors: MonsterDoorStrategy = spec.doors
  
  /** Anything that happens before a monster's next action.
    */
  def beforeNextAction(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    if (breeds) {
      maybeBreed(accessor, callback)
    }

    List.empty
  }

  private def maybeBreed(accessor: GameAccessor, callback: GameCallback): Unit = {
    // 1 in 8 chance of breeding
    if (Random.nextInt(8) == 0) {
      accessor.level.emptyAdjacentFloorTileCoordinates(coordinates) match {
        case Some(coordinates) =>
          val monster = factory(new Random(), coordinates, accessor.legendarium.armory)
          monster.awake = true
          accessor.level
            .tile(coordinates)
            .asInstanceOf[Floor]
            .setOccupant(monster)
        case None =>
      }
    }

    List.empty
  }

  override def onNextTurn(): Unit = {
    regenerateEnergy()
  }

  def getAction(game: GameAccessor): MonsterAction = spec.actions.select(this, game)

  def color: Color = spec.color

  def displayName: String = name.toLowerCase

  override def toString: String = {
    s"$name($health, ${inventory.mkString("[", ",", "]")})"
  }
}
object Monster {

  def apply(random: Random, factory: MonsterFactory, coordinates: Coordinates, armory: Armory): Monster = {
    val awake = factory.spec.sleepiness == 0
    val inventory = mutable.ListBuffer.from(factory.spec.generateStartingInventory(random, armory))
    new Monster(factory, coordinates, factory.spec.health.roll(), awake, inventory)
  }

  /** All monsters start with some energy,  less than the player enters a level with.
    */
  def startingEnergy(): Int = Random.nextInt(NormalSpeed - 1) + 1
}
