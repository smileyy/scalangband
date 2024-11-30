package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.tile.{DownStairs, OccupiableTile, UpStairs}
import scalangband.model.util.RandomUtils.randomElement
import scalangband.model.util.TileUtils.allCoordinatesFor

object GoDownStairsAction extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = {
    game.playerTile match {
      case _: DownStairs =>
        // this is a little wonky...
        if (game.level.depth == 0) {
          game.playerTile.clearOccupant()
        }

        val newLevel = game.levelGenerator.generateLevel(game.random, game.level.depth + 1)
        val startingCoordinates = randomElement(game.random, allCoordinatesFor(newLevel.tiles, tile => tile.isInstanceOf[UpStairs]))
        newLevel(startingCoordinates).asInstanceOf[OccupiableTile].setOccupant(game.player)
        game.playerCoordinates = startingCoordinates
        game.level = newLevel
        Some(MessageResult(s"You descend to ${newLevel.depth * 50} feet"))
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
        val startingCoordinates = randomElement(game.random, allCoordinatesFor(newLevel.tiles, tile => tile.isInstanceOf[DownStairs]))
        newLevel(startingCoordinates).asInstanceOf[OccupiableTile].setOccupant(game.player)
        game.playerCoordinates = startingCoordinates
        game.level = newLevel
        val message = if (newLevel.depth == 0) "You return to town" else s"You ascend to ${newLevel.depth * 50} feet"
        Some(MessageResult(message))
      case _ => Some(MessageResult("There are no up stairs here"))
    }
  }
}