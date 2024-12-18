package scalangband.model.level

import scalangband.model.Game.MaxDungeonDepth
import scalangband.model.item.{Armory, Item}
import scalangband.model.location.Coordinates
import scalangband.model.monster.{Bestiary, Monster, MonsterFactory}
import scalangband.model.tile.*
import scalangband.model.util.RandomUtils.randomElement
import scalangband.model.util.TileUtils.allCoordinatesFor

import scala.util.Random

class DungeonLevelBuilder(random: Random, val tiles: Array[Array[Tile]], armory: Armory, bestiary: Bestiary) { outer =>
  def height: Int = tiles.length
  def width: Int = tiles(0).length

  def apply(row: Int, col: Int): Tile = tiles(row)(col)
  def setTile(coordinates: Coordinates, tile: Tile): Unit = setTile(coordinates.row, coordinates.col, tile)
  def setTile(row: Int, col: Int, tile: Tile): Unit = tiles(row)(col) = tile

  def getCanvas(row: Int, col: Int, height: Int, width: Int): Option[DungeonLevelCanvas] = {
    if (wouldNotOverlap(row, col, height, width)) {
      Some(new Canvas(row, col, height, width))
    } else None
  }

  private class Canvas(dy: Int, dx: Int, val height: Int, val width: Int) extends DungeonLevelCanvas {
    override def getTile(row: Int, col: Int): Tile = tiles(row + dy)(col + dx)

    override def setTile(row: Int, col: Int, tile: Tile): DungeonLevelCanvas = {
      outer.tiles(row + dy)(col + dx) = tile
      this
    }

    override def addMonster(row: Int, col: Int, factory: MonsterFactory): DungeonLevelCanvas = {
      val monster = factory.apply(outer.random, Coordinates(row + dy, col + dx), armory)
      getTile(row, col).asInstanceOf[OccupiableTile].setOccupant(monster)

      this
    }
  }

  private def wouldNotOverlap(row: Int, col: Int, height: Int, width: Int): Boolean = {
    tiles.slice(row, row + height)
      .flatMap(row => row.slice(col, col + width))
      .forall(tile => tile.isInstanceOf[RemovableWall])
  }

  def build[T <: DungeonLevel](random: Random, depth: Int, createLevel: Array[Array[Tile]] => T): T = {
    enforceStairInvariants(random, depth)

    createLevel(tiles)
  }

  /** Enforces the correct number of up and down stairs for the level's depth
    */
  private def enforceStairInvariants(random: Random, depth: Int): Unit = {
    enforceUpStairsInvariants(random, depth)
    enforceDownStairsInvariants(random, depth)
  }

  private def enforceUpStairsInvariants(random: Random, depth: Int): Unit = {
    val upStairs = allCoordinatesFor(tiles, _.isInstanceOf[UpStairs])
    if (depth == 0) {
      // replace any up stairs on the town level with floor tiles
      upStairs.foreach(coords => setTile(coords, Floor.empty()))
    } else if (upStairs.length == 0) {
      (0 until desiredNumberOfUpStairs(random)).foreach { _ =>
        val floor = randomElement(
          random,
          allCoordinatesFor(tiles, tile => tile.isInstanceOf[Floor] && !tile.asInstanceOf[Floor].occupied)
        )
        setTile(floor, new UpStairs())
      }
    }
  }

  private def desiredNumberOfUpStairs(random: Random) = {
    random.nextInt(2) + 1
  }

  private def enforceDownStairsInvariants(random: Random, depth: Int): Unit = {
    val downStairs = allCoordinatesFor(tiles, _.isInstanceOf[DownStairs])
    if (depth == MaxDungeonDepth) {
      // replace any up stairs on the bottom level with floor tiles
      downStairs.foreach(coords => setTile(coords, Floor.empty()))
    } else if (downStairs.length == 0) {
      (0 until desiredNumberOfDownStairs(random, depth)).foreach { _ =>
        val floor = randomElement(
          random,
          allCoordinatesFor(tiles, tile => tile.isInstanceOf[Floor] && !tile.asInstanceOf[Floor].occupied)
        )
        setTile(floor, new DownStairs())
      }
    }
  }

  private def desiredNumberOfDownStairs(random: Random, depth: Int) = {
    if (depth == 0) 1 else random.nextInt(3) + 1
  }
}
object DungeonLevelBuilder {

  /** Creates a new builder of the given height and width. The builder starts out as a boundary of a [[PermanentWall]],
   * filled by [[RemovableWall]]s.
   */
  def apply(random: Random, armory: Armory, bestiary: Bestiary, height: Int = 50, width: Int = 100): DungeonLevelBuilder = {
    val tiles = Array.ofDim[Tile](height, width)

    for (row <- 0 until height) {
      for (col <- 0 until width) {
        tiles(row)(col) = {
          if (row == 0 || col == 0 || row == height - 1 || col == width - 1) {
            new PermanentWall()
          } else {
            new RemovableWall()
          }
        }
      }
    }

    new DungeonLevelBuilder(random, tiles, armory, bestiary)
  }
}

trait DungeonLevelCanvas {
  def height: Int
  def width: Int

  def setTile(row: Int, col: Int, tile: Tile): DungeonLevelCanvas
  def getTile(row: Int, col: Int): Tile

  def fill(row: Int = 0, col: Int = 0, height: Int = height, width: Int = width, factory: () => Tile): DungeonLevelCanvas = {
    for (rowIdx <- row until height) {
      for (colIdx <- col until width) {
        setTile(rowIdx, colIdx, factory())
      }
    }
    this
  }

  def addMonster(row: Int, col: Int, factory: MonsterFactory): DungeonLevelCanvas
}