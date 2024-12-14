package scalangband.model.player.action

import org.slf4j.LoggerFactory
import scalangband.model.player.action.PlayerMovementAction.Logger
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.effect.Confusion
import scalangband.model.location.Direction
import scalangband.model.monster.Monster
import scalangband.model.tile.*
import scalangband.model.{GameAccessor, GameCallback}

object PlayerPassAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = List.empty
}

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
      case _: Wall => MessageResult("There is a wall in the way!")
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

object GoDownStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case _: DownStairs =>
        callback.moveDownTo(accessor.level.depth + 1)
        List(MessageResult(s"You enter a maze of down staircases."))
      case _ => List(MessageResult("I see no down staircase here."))
    }
  }
}

object GoUpStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case _: UpStairs =>
        callback.moveUpTo(accessor.level.depth - 1)
        val message = "You enter a maze of up staircases."
        List(MessageResult(message))
      case _ => List(MessageResult("I see no up staircase here."))
    }
  }
}

object PickUpItemAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    accessor.playerTile match {
      case floor: Floor if floor.items.nonEmpty =>
        val item = floor.items.head
        callback.playerPickup(floor, floor.items.head)
        List(MessageResult(s"You pick up the ${item.displayName}"))
      case _ => List(MessageResult("There is nothing to pick up"))
    }
  }
}

object ListInventoryAction extends InterfaceAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.logInventory()
    List.empty
  }
}

object ListEquipmentAction extends InterfaceAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.logEquipment()
    List.empty
  }
}