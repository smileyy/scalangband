package scalangband.model.level

import scalangband.model.level.Town.{TownHeight, TownWidth}
import scalangband.model.location.Coordinates
import scalangband.model.tile.{Floor, PermanentWall, Tile}

import scala.util.Random

class Town(tiles: Array[Array[Tile]]) extends Level(0, tiles)
object Town {
  private val TownHeight = 36
  private val TownWidth = 60

  def apply(random: Random): Town = {
    val builder = LevelBuilder(TownHeight, TownWidth, 0)

    for (row <- 1 until TownHeight - 1) {
      for (col <- 1 until TownWidth - 1) {
        builder.setTile(row, col, Floor.empty())
      }
    }

    new Town(builder.build(random).tiles)
  }
}