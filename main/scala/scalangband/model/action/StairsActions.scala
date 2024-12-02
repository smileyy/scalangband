package scalangband.model.action

import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.tile.{DownStairs, UpStairs}
import scalangband.model.{GameAccessor, GameCallback}

object GoDownStairsAction extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    accessor.playerTile match {
      case _: DownStairs =>
        // this is a little wonky...
//        if (accessor.level.depth == 0) {
//          game.level(game.player.coordinates).asInstanceOf[OccupiableTile].clearOccupant()
//        }

        callback.moveDownTo(accessor.level.depth + 1)
        Some(MessageResult(s"You descend to ${accessor.level.depth * 50} feet"))
      case _ => Some(MessageResult("There are no down stairs here"))
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
        Some(MessageResult(message))
      case _ => Some(MessageResult("There are no up stairs here"))
    }
  }
}