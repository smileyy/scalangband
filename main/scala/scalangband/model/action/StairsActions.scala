package scalangband.model.action

import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.tile.{DownStairs, UpStairs}
import scalangband.model.{GameAccessor, GameCallback}

object GoDownStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case _: DownStairs =>
        callback.moveDownTo(accessor.level.depth + 1)
        Some(MessagesResult(List(s"You enter a maze of down staircases.")))
      case _ => Some(MessagesResult(List("I see no down staircase here."), false))
    }
  }
}

object GoUpStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case _: UpStairs =>
        callback.moveUpTo(accessor.level.depth - 1)
        val message = 
          if (accessor.level.depth == 0) "You enter a maze of up staircases."
        Some(MessagesResult(List(message)))
      case _ => Some(MessagesResult(List("I see no up staircase here."), false))
    }
  }
}