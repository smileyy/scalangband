package scalangband.model.level.room.rectangle

import scalangband.model.level.room.{AbstractRoom, Room, RoomGenerator}
import scalangband.model.location.*
import scalangband.model.monster.Bestiary
import scalangband.model.tile.{Floor, RemovableWall, Tile}
import scalangband.model.util.RandomUtils.{randomBetween, randomElement}

import scala.util.Random

object RectangularRoomGenerator extends RoomGenerator {
  private val MinHeight = 5
  private val MaxHeight = 16

  private val MinWidth = 5
  private val MaxWidth = 24
  
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val height = randomBetween(random, MinHeight, MaxHeight)
    val width = randomBetween(random, MinWidth, MaxWidth)
    RectangularRoom(rowOffset, colOffset, height, width, depth)
  }
}
class RectangularRoom(tiles: Array[Array[Tile]], top: Int, left: Int, effectiveDepth: Int) 
  extends AbstractRoom(tiles, top, left, effectiveDepth) {

  private def topWall: Array[Coordinates] = (left + 1 until left + width).map(col => Coordinates(top, col)).toArray
  private def bottomWall: Array[Coordinates] = (left + 1 until left + width).map(col => Coordinates(bottom, col)).toArray
  private def leftWall: Array[Coordinates] = (top + 1 until top + height).map(row => Coordinates(row, left)).toArray
  private def rightWall: Array[Coordinates] = (top + 1 until top + height).map(row => Coordinates(row, right)).toArray
  
  override def getAttachmentPoint(random: Random, direction: Direction): Coordinates = direction match {
    case UpDirection => randomElement(random, topWall)
    case DownDirection => randomElement(random, bottomWall)
    case LeftDirection => randomElement(random, leftWall)
    case RightDirection => randomElement(random, rightWall)
  }
}
object RectangularRoom {
  def apply(top: Int, left: Int, height: Int, width: Int, effectiveDepth: Int): Room = {
    val tiles = Array.ofDim[Tile](height, width)

    for (rowIdx <- 0 until height) {
      for (colIdx <- 0 until width) {
        tiles(rowIdx)(colIdx) = {
          if (rowIdx == 0 || rowIdx == height - 1 || colIdx == 0 || colIdx == width - 1) new RemovableWall()
          else Floor.empty()
        }
      }
    }

    new RectangularRoom(tiles, top, left, effectiveDepth)
  }
}