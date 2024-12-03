package scalangband.model.action

import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.tile.{DownStairs, UpStairs}
import scalangband.model.{GameAccessor, GameCallback}

object GoDownStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case _: DownStairs =>
        callback.moveDownTo(accessor.level.depth + 1)
        Some(MessagesResult(List(s"You descend to ${accessor.level.depth * 50} feet")))
      case _ => Some(MessagesResult(List("There are no down stairs here"), false))
    }
  }
}

object GoUpStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case _: UpStairs =>
        callback.moveUpTo(accessor.level.depth - 1)
        val message = 
          if (accessor.level.depth == 0) "You return to town" else s"You ascend to ${accessor.level.depth * 50} feet"
        Some(MessagesResult(List(message)))
      case _ => Some(MessagesResult(List("There are no up stairs here"), false))
    }
  }
}