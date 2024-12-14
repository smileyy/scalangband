package scalangband.model.player.action

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.effect.Confusion
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.player.action.PlayerMovementAction.Logger
import scalangband.model.tile.*
import scalangband.model.{GameAccessor, GameCallback}

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
        results = callback.player.attack(ot.occupant.get.asInstanceOf[Monster], callback) ::: results
      case floor: Floor if floor.items.nonEmpty =>
        callback.movePlayerTo(targetCoordinates)

        floor.money.foreach(money => {
          floor.removeItem(money)
          callback.player.addMoney(money.amount)
          results = MessageResult(s"You have found ${money.amount} gold pieces worth of ${money.material}.") :: results
        })

        if (floor.items.isEmpty) {} // it is possible that the floor only had money in it, and now it is all gone
        else if (floor.items.size == 1) results = MessageResult(s"You see ${floor.items.head.displayName}.") :: results
        else results = MessageResult("You see a pile of items.") :: results
      case ot: OccupiableTile =>
        callback.movePlayerTo(targetCoordinates)
    }

    results
  }

  override def toString: String = s"${getClass.getSimpleName}($intendedDirection)"
}
object PlayerMovementAction {
  private val Logger = LoggerFactory.getLogger(classOf[PlayerMovementAction])
}
