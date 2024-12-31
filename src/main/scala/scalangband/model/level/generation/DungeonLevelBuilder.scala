package scalangband.model.level.generation

import scalangband.model.Game.MaxDungeonDepth
import scalangband.model.Legendarium
import scalangband.model.item.Armory
import scalangband.model.level.{DungeonLevel, Tiled}
import scalangband.model.location.Coordinates
import scalangband.model.monster.*
import scalangband.model.tile.*
import scalangband.model.util.RandomUtils.randomElement
import scalangband.model.util.{RandomUtils, TileUtils}

import scala.util.Random

class DungeonLevelBuilder(random: Random, val tiles: Array[Array[Tile]], legendarium: Legendarium)
    extends Tiled { outer =>
  def height: Int = tiles.length
  def width: Int = tiles(0).length

  def apply(row: Int, col: Int): Tile = tiles(row)(col)
  def setTile(coordinates: Coordinates, tile: Tile): Unit = setTile(coordinates.row, coordinates.col, tile)
  def setTile(row: Int, col: Int, tile: Tile): Unit = tiles(row)(col) = tile

  def slice(top: Int, left: Int, height: Int, width: Int): Array[Array[Tile]] = {
    tiles.slice(top, top + height).map(row => row.slice(left, left + width))
  }

  def getCanvas(row: Int, col: Int, height: Int, width: Int): DungeonLevelCanvas = {
    new Canvas(row, col, height, width)
  }

  private class Canvas(dy: Int, dx: Int, val height: Int, val width: Int) extends DungeonLevelCanvas {
    override def getTile(row: Int, col: Int): Tile = tiles(row + dy)(col + dx)

    override def setTile(row: Int, col: Int, tile: Tile): DungeonLevelCanvas = {
      outer.tiles(row + dy)(col + dx) = tile
      this
    }

    override def addMonster(depth: Int): Unit = {
      val (row, col) = RandomUtils
        .randomPairs(random, height, width)
        .filter((r, c) =>
          getTile(r, c) match {
            case ot: OccupiableTile if !ot.occupied => true
            case _ => false
          }
        )
        .head

      addMonster(row, col, depth)
    }

    override def addMonster(row: Int, col: Int, depth: Int): Unit = {
      addMonster(row: Int, col: Int, legendarium.getMonsterFactory(random, depth), depth)
    }

    override def addMonster(row: Int, col: Int, factory: MonsterFactory, depth: Int): Unit = {
      val coordinates = Coordinates(row + dy, col + dx)
      addSingleMonster(factory, coordinates)
      factory.spec.friends.foreach(friendSpec => addFriends(friendSpec, coordinates, depth, factory.spec.depth))
    }

    private def addSingleMonster(factory: MonsterFactory, coordinates: Coordinates): Unit = {
      val monster = factory.apply(outer.random, coordinates, legendarium.armory)
      outer.tiles(coordinates.row)(coordinates.col).asInstanceOf[OccupiableTile].setOccupant(monster)
    }

    private def addFriends(spec: MonsterFriendSpec, start: Coordinates, depth: Int, originatingMonsterDepth: Int): Unit = {
      if (random.nextInt(100) < spec.probability) {

        val maybeFactory: Option[MonsterFactory] = spec match {
          case factorySpec: MonsterFactoryFriendSpec => Some(factorySpec.factory)
          case archetypeSpec: MonsterArchetypeFriendSpec => legendarium.getMonsterFactory(random, archetypeSpec.archetype, depth)
        }

        maybeFactory.foreach { factory =>
          val numberOfFriends = if (depth < originatingMonsterDepth + 5) {
            spec.number.roll() / 2
          } else {
            spec.number.roll()
          }

          val filter: Tile => Boolean = tile => tile.isInstanceOf[OccupiableTile] && !tile.occupied
          val locations = getAdjacentCoordinates(start, filter, numberOfFriends)

          locations.foreach(coords => addSingleMonster(factory, coords))
        }
      }
    }
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
    val upStairs = allCoordinatesFor(_.isInstanceOf[UpStairs])
    if (depth == 0) {
      // replace any up stairs on the town level with floor tiles
      upStairs.foreach(coords => setTile(coords, Floor.empty()))
    } else if (upStairs.length == 0) {
      (0 until desiredNumberOfUpStairs(random)).foreach { _ =>
        val floor = randomElement(
          random,
          allCoordinatesFor(tile => tile.isInstanceOf[Floor] && !tile.asInstanceOf[Floor].occupied)
        )
        setTile(floor, new UpStairs())
      }
    }
  }

  private def desiredNumberOfUpStairs(random: Random) = {
    random.nextInt(2) + 1
  }

  private def enforceDownStairsInvariants(random: Random, depth: Int): Unit = {
    val downStairs = allCoordinatesFor(_.isInstanceOf[DownStairs])
    if (depth == MaxDungeonDepth) {
      // replace any up stairs on the bottom level with floor tiles
      downStairs.foreach(coords => setTile(coords, Floor.empty()))
    } else if (downStairs.length == 0) {
      (0 until desiredNumberOfDownStairs(random, depth)).foreach { _ =>
        val floor = randomElement(
          random,
          allCoordinatesFor(tile => tile.isInstanceOf[Floor] && !tile.asInstanceOf[Floor].occupied)
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
  def apply(
      random: Random,
      legendarium: Legendarium,
      height: Int = 50,
      width: Int = 100
  ): DungeonLevelBuilder = {
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

    new DungeonLevelBuilder(random, tiles, legendarium)
  }
}

trait DungeonLevelCanvas {
  def height: Int
  def width: Int

  def setTile(row: Int, col: Int, tile: Tile): DungeonLevelCanvas
  def getTile(row: Int, col: Int): Tile

  def fillRect(row: Int = 0, col: Int = 0, height: Int = height, width: Int = width, factory: () => Tile): Unit = {
    for (rowIdx <- row until row + height) {
      for (colIdx <- col until col + width) {
        setTile(rowIdx, colIdx, factory())
      }
    }
  }

  def drawHLine(row: Int, col: Int, length: Int, factory: () => Tile): Unit = {
    for (colIdx <- col until col + length) {
      setTile(row, colIdx, factory())
    }
  }

  def drawVLine(row: Int, col: Int, length: Int, factory: () => Tile): Unit = {
    for (rowIdx <- row until row + length) {
      setTile(rowIdx, col, factory())
    }
  }

  def addMonster(depth: Int): Unit
  def addMonster(row: Int, col: Int, depth: Int): Unit
  def addMonster(row: Int, col: Int, factory: MonsterFactory, depth: Int): Unit
}
