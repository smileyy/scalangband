package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.tile.{DownStairs, UpStairs}

object GoDownStairsAction extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = {
    game.playerTile match {
      case _: DownStairs =>
        // this is a little wonky...
        if (game.level.isTown) {
          game.playerTile.clearOccupant()
        }

        val newLevel = game.levelGenerator.generateLevel(game.random, game.level.depth + 1)
        val startingTile = newLevel.randomTile(game.random, classOf[UpStairs])
        startingTile.setOccupant(game.player)
        game.playerCoordinates = startingTile.coordinates
        game.level = newLevel
        Some(MessageResult(s"You descend to ${newLevel.depthString}"))
      case _ => Some(MessageResult("There are no down stairs here"))
    }
  }
}

object GoUpStairsAction extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = {
    game.playerTile match {
      case _: UpStairs =>
        val newLevel = if (game.level.depth == 1) {
          game.town
        } else {
          game.levelGenerator.generateLevel(game.random, game.level.depth)
        }
        val startingTile = newLevel.randomTile(game.random, classOf[DownStairs])
        startingTile.setOccupant(game.player)
        game.playerCoordinates = startingTile.coordinates
        game.level = newLevel
        val message = if (newLevel.isTown) "You return to town" else s"You ascend to ${newLevel.depthString}"
        Some(MessageResult(message))
      case _ => Some(MessageResult("There are no up stairs here"))
    }
  }
}