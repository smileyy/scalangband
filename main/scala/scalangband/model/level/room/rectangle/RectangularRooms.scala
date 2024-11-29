package scalangband.model.level.room.rectangle

import scalangband.model.level.room.{AbstractRoom, Room, RoomGenerator}
import scalangband.model.location.*
import scalangband.model.tile.{Floor, RemovableWall, Tile}
import scalangband.util.ArrayUtils.randomElement
import scalangband.util.RandomUtils.randomBetween

import scala.util.Random

object RectangularRoomGenerator extends RoomGenerator {
  val MinHeight = 5
  val MaxHeight = 16

  val MinWidth = 5
  val MaxWidth = 24
  
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val height = randomBetween(random, MinHeight, MaxHeight)
    val width = randomBetween(random, MinWidth, MaxWidth)
    RectangularRoom(rowOffset, colOffset, height, width)
  }
}
class RectangularRoom(tiles: Array[Array[Tile]]) extends AbstractRoom(tiles) {
  def topWall: Array[Coordinates] = tiles.flatten.map(_.coordinates)
    .filter { c => c.rowIdx == top && c.colIdx != left && c.colIdx != right }
  def bottomWall: Array[Coordinates] = tiles.flatten.map(_.coordinates)
    .filter { c => c.rowIdx == bottom && c.colIdx != left && c.colIdx != right }
  def leftWall: Array[Coordinates] = tiles.flatten.map(_.coordinates)
    .filter { c => c.colIdx == left && c.rowIdx != top && c.rowIdx != bottom }
  def rightWall: Array[Coordinates] = tiles.flatten.map(_.coordinates)
    .filter { c => c.colIdx == right && c.rowIdx != top && c.rowIdx != bottom }
  
  override def getAttachmentPoint(random: Random, direction: Direction): Coordinates = direction match {
    case Up => randomElement(random, topWall)
    case Down => randomElement(random, bottomWall)
    case Left => randomElement(random, leftWall)
    case Right => randomElement(random, rightWall)
  }
}
object RectangularRoom {
  def apply(rowOffset: Int, colOffset: Int, height: Int, width: Int): Room = {
    val tiles = Array.ofDim[Tile](height, width)

    for (rowIdx <- 0 until height) {
      for (colIdx <- 0 until width) {
        tiles(rowIdx)(colIdx) = {
          val coordinates = Coordinates(rowIdx + rowOffset, colIdx + colOffset)
          if (rowIdx == 0 || rowIdx == height - 1 || colIdx == 0 || colIdx == width - 1) new RemovableWall(coordinates)
          else Floor.empty(coordinates)
        }
      }
    }

    new RectangularRoom(tiles)
  }
}