package scalangband.model.player.action

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.effect.{Confusion, Fear}
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.player.action.PlayerMovementAction.Logger
import scalangband.model.tile.*
import scalangband.model.util.DiceRoll
import scalangband.model.{GameAccessor, GameCallback}

import scala.util.Random

case class PlayerMovementAction(intendedDirection: Direction) extends PhysicalAction {
  def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    val currentCoordinates = accessor.player.coordinates

    val actualDirection = if (accessor.player.hasEffect(Confusion)) {
      results = Confusion.affectedResult :: results
      val direction = Direction.randomDirection()
      Logger.debug(s"Player is trying to move $intendedDirection but is confused and is moving $direction")
      direction
    } else {
      Logger.debug(s"Player is moving $intendedDirection")
      intendedDirection
    }

    val targetCoordinates = currentCoordinates + actualDirection
    val targetTile = accessor.level.tile(targetCoordinates)

    targetTile match {
      case _: Wall =>
        results = MessageResult("There is a wall in the way!") :: results
      case _: ClosedDoor =>
        callback.level.replaceTile(targetCoordinates, new OpenDoor())
      case ot: OccupiableTile if ot.occupied =>
        results = attack(accessor, callback, ot.occupant.get.asInstanceOf[Monster]) ::: results
      case floor: Floor if floor.items.nonEmpty =>
        callback.movePlayerTo(targetCoordinates)

        floor.money.foreach(money => {
          floor.removeItem(money)
          callback.player.addMoney(money.amount)
          results = MessageResult(s"You have found ${money.amount} gold pieces worth of ${money.material}.") :: results
        })

        if (floor.items.isEmpty) {} // it is possible that the floor only had money in it, and now it is all gone
        else if (floor.items.size == 1) results = MessageResult(s"You see ${floor.items.head}.") :: results
        else results = MessageResult("You see a pile of items.") :: results
      case ot: OccupiableTile =>
        callback.movePlayerTo(targetCoordinates)
    }

    results
  }

  private def attack(accessor: GameAccessor, callback: GameCallback, monster: Monster): List[ActionResult] = {
    if (accessor.player.hasEffect(Fear)) {
      List(MessageResult(s"You are too afraid to attack the ${monster.displayName}!"))
    } else {
      Random.nextInt(20) + 1 match {
        case 1  => handleMiss(monster)
        case 20 => handleHit(accessor, callback, monster)
        case _ =>
          if (Random.nextInt(accessor.player.toHit) > monster.armorClass * 3 / 4) {
            handleHit(accessor, callback, monster)
          } else {
            handleMiss(monster)
          }
      }
    }
  }

  private def handleHit(accessor: GameAccessor, callback: GameCallback, monster: Monster) = {
    var results: List[ActionResult] = List.empty

    val damageDice = accessor.player.equipment.weapon match {
      case Some(weapon) => weapon.damage
      case None         => DiceRoll("1d1")
    }
    val damage = Math.max(damageDice.roll() + accessor.player.toDamage, 1)
    monster.health = monster.health - damage
    monster.awake = true

    results = MessageResult(s"You hit the ${monster.displayName}.") :: results
    Logger.info(s"Player hit ${monster.displayName} for $damage damage")
    if (monster.health <= 0) {
      Logger.info(s"Player killed ${monster.displayName}")
      results =
        MessageResult(s"You have ${if (monster.alive) "slain" else "destroyed"} the ${monster.displayName}.") :: results
      results = callback.killMonster(monster) ::: results

      results = callback.player.addExperience(monster.experience) ::: results
    }

    results
  }

  private def handleMiss(monster: Monster): List[ActionResult] = {
    Logger.info(s"Player missed ${monster.displayName}")
    List(MessageResult(s"You miss the ${monster.displayName}."))
  }
}
object PlayerMovementAction {
  private val Logger = LoggerFactory.getLogger(classOf[PlayerMovementAction])
}
