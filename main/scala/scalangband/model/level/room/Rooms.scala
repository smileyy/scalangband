package scalangband.model.level.room

import org.slf4j.LoggerFactory
import scalangband.model.level.room.Room.Logger
import scalangband.model.level.{Level, LevelRanges}
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.monster.{Bestiary, Monster}
import scalangband.model.tile.{Floor, OccupiableTile, Tile}
import scalangband.model.util.RandomUtils

import scala.util.Random

trait Room {
  def tiles: Array[Array[Tile]]

  def height: Int = tiles.length

  def width: Int = tiles(0).length

  def apply(row: Int, col: Int): Tile = tiles(row)(col)

  def top: Int

  def bottom: Int = top + height - 1

  def left: Int

  def right: Int = left + width - 1

  /**
   * Returns a place where a hallway can be attached to enter the room. This method may throw an Exception if the room
   * should be detached, which is a rare case. So, uh, always check that a room is supposed to be attached before
   * checking for an attachment point.
   */
  def getAttachmentPoint(random: Random, direction: Direction): Coordinates

  /**
   * Replaces the tile at the given indices.
   */
  def setTile(row: Int, col: Int, tile: Tile): Unit

  /**
   * Whether this room should be attached to the rest of the level. If a room is to be attached, it must provide 
   * possible attachment points for each direction.
   */
  def attached: Boolean = true

  def addMonsters(random: Random, bestiary: Bestiary): Unit

  def generateMonster(random: Random, bestiary: Bestiary, depth: Int, coordinates: Coordinates): Monster = {
    val monster = bestiary.generateMonster(random, depth, coordinates).get
    Logger.debug(s"Generated $monster")
    monster
  }
}

abstract class AbstractRoom(val tiles: Array[Array[Tile]], val top: Int, val left: Int, val effectiveDepth: Int) extends Room {
  override def setTile(row: Int, col: Int, tile: Tile): Unit = {
    tiles(row)(col) = tile
  }

  override def addMonsters(random: Random, bestiary: Bestiary): Unit = {
    val numberOfMonsters = random.nextInt(100) match {
      case x if x < 25 => 0
      case x if x < 75 => 1
      case x if x < 95 => 2
      case x if x < 100 => 3
    }

    (0 until numberOfMonsters).foreach(_ => {
      val (row, col) = RandomUtils.randomPairs(random, height, width).filter((r, c) => tiles(r)(c) match {
        case ot: OccupiableTile if !ot.occupied => true
        case _ => false
      }).head

      val monsterDepth: Int = LevelRanges.ranges(effectiveDepth).randomInRange(random)

      val monster = generateMonster(random, bestiary, monsterDepth, Coordinates(row + top, col + left))
      tiles(row)(col).asInstanceOf[OccupiableTile].setOccupant(monster)
    }
    )
  }
}
object Room {
  private val Logger = LoggerFactory.getLogger(classOf[AbstractRoom])
}