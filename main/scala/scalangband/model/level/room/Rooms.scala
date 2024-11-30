package scalangband.model.level.room

import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.tile.Tile

import scala.util.Random

trait Room {
  def tiles: Array[Array[Tile]]
  def height: Int = tiles.length
  def width: Int = tiles(0).length  
  
  def apply(rowIdx: Int, colIdx: Int): Tile = tiles(rowIdx)(colIdx)

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
   * Replaces the tile at the given coordinates.
   */
  def setTile(rowIdx: Int, colIdx: Int, tile: Tile): Unit

  /**
   * Whether this room should be attached to the rest of the level. If a room is to be attached, it must provide 
   * possible attachment points for each direction.
   */
  def attached: Boolean = true
}

abstract class AbstractRoom(val tiles: Array[Array[Tile]], val top: Int, val left: Int) extends Room {
  override def setTile(rowIdx: Int, colIdx: Int, tile: Tile): Unit = {
    val original = apply(rowIdx, colIdx)
    
    tiles(rowIdx)(colIdx) = tile
  }
}