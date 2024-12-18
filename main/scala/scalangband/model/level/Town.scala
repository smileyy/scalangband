package scalangband.model.level

import scalangband.data.monster.person.RandomlyMumblingTownsperson
import scalangband.model.item.Armory
import scalangband.model.monster.Bestiary
import scalangband.model.tile.{Floor, RemovableWall, Tile}

import scala.util.Random

class Town(tiles: Array[Array[Tile]]) extends DungeonLevel(0, tiles)
object Town {
  private val TownHeight = 36
  private val TownWidth = 80

  def apply(random: Random, armory: Armory, bestiary: Bestiary): Town = {
    val builder = DungeonLevelBuilder(random, armory, bestiary, TownHeight, TownWidth)

    builder.getCanvas(1, 1, TownHeight - 2, TownWidth - 2).foreach { canvas =>
      canvas.fill(factory = () => Floor.empty()).addMonster(0, 0, RandomlyMumblingTownsperson)
    }

    builder.build(random, 0, tiles => new Town(tiles))
  }
}