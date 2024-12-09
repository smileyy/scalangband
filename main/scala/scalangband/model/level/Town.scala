package scalangband.model.level

import scalangband.data.monster.person.RandomlyMumblingTownsperson
import scalangband.model.tile.{Floor, Tile}

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

    builder.addMonster(1, 1, coords => RandomlyMumblingTownsperson(coords, random))
    
    builder.build(random, (depth, tiles) => new Town(tiles)).asInstanceOf[Town]
  }
}