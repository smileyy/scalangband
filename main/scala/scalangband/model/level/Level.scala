package scalangband.model.level

import scalangband.model.location.Coordinates
import scalangband.model.tile.{Floor, OccupiableTile, Stairs, Tile}
import scalangband.util.ArrayUtils.randomElement

import scala.reflect.ClassTag
import scala.util.Random

class Level(val tiles: Array[Array[Tile]], val depth: Int) {
  val height: Int = tiles.length
  val width: Int = tiles(0).length
  
  val isTown: Boolean = depth == 0

  /**
   * The depth string to be displayed. It's a little weird to have a presentation-oriented value here, but the string
   * is used by stairs actions to display the level depth being ascended/descended to.
   */
  val depthString: String = depth match {
    case 0 => "Town"
    case x => x * 50 + " feet"
  }

  def apply(coordinates: Coordinates): Tile = tiles(coordinates.rowIdx)(coordinates.colIdx)
  def apply(rowIdx: Int, colIdx: Int): Tile = tiles(rowIdx)(colIdx)

  def markTilesInvisible(): Unit = {
    for (rowIdx <- 0 until height) {
      for (colIdx <- 0 until width) {
        tiles(rowIdx)(colIdx).setVisible(false)
      }
    }
  }
  
  def randomTile[T <: Tile : ClassTag](random: Random, tileType: Class[T]): T = {
    randomElement(random, tiles.flatten.filter(t => t.getClass == tileType)).asInstanceOf[T]
  }

  /**
   * Creates stairs while retaining any creature that is on the tile. Necessary because stairs are created after
   * a level is populated with mobs, and we don't want to wipe out a generated mob.
   */
  def replaceWithStairs(originalTile: Floor, stairs: Stairs): Unit = {
    if (originalTile.occupant.isDefined) {
      stairs.setOccupant(originalTile.occupant.get)
    }
    
    setTile(originalTile.coordinates, stairs)
  }
  
  def setTile(coordinates: Coordinates, tile: Tile): Unit = {
    tiles(coordinates.rowIdx)(coordinates.colIdx) = tile
  }
}
