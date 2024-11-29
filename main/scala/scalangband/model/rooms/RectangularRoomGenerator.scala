package scalangband.model.rooms

import scalangband.model.location.Coordinates
import scalangband.model.tile.{Floor, RemovableWall, Tile}

import scala.util.Random

class RectangularRoomGenerator extends RoomGenerator {
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val room = generateEmptyRoom(random.nextInt(13) + 5, random.nextInt(9) + 5, rowOffset, colOffset)

    room
  }

  def generateEmptyRoom(width: Int, height: Int, rowOffset: Int, colOffset: Int): Room = {
    Room(rowOffset, colOffset, emptyRectangularRoomTiles(width, height, rowOffset, colOffset))
  }

  def numberOfExitsToGenerate(random: Random): Int = {
    random.nextInt(3) + 1
  }

  /**
   * Generates an empty room, of the given width and height, surrounded by walls, which are included in the width and
   * height.
   */
  private def emptyRectangularRoomTiles(width: Int, height: Int, rowOffset: Int, colOffset: Int): Array[Array[Tile]] = {
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

    tiles
  }

}
