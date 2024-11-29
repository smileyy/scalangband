package scalangband.model.rooms

import scalangband.model.location.Coordinates
import scalangband.model.tile.{ClosedDoor, Floor, RemovableWall, Wall}

import scala.util.Random

class SurroundedByHallwayRoomGenerator extends RoomGenerator {
  private val interiorWidth = 19
  private val interiorHeight = 7

  private val delegate = new RectangularRoomGenerator()

  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    generateEmptyRoom(random, rowOffset, colOffset)
  }

  def generateEmptyRoom(random: Random, rowOffset: Int, colOffset: Int): Room = {
    val room = delegate.generateEmptyRoom(interiorWidth + 6, interiorHeight + 6, rowOffset, colOffset)

    // add walls for interior room
    for (rowIdx <- 2 until room.height - 2) {
      for (colIdx <- 2 until room.width - 2) {
        if (rowIdx == 2 || rowIdx == room.height - 3 || colIdx == 2 || colIdx == room.width - 3) {
          room.setTile(rowIdx, colIdx, new RemovableWall(Coordinates(rowIdx + rowOffset, colIdx + colOffset)))
        }
      }
    }

    // add internal door
    random.nextInt(4) match {
      case 0 => room.setTile(2, 12, new ClosedDoor(Coordinates(2 + rowOffset, 12 + colOffset))) // top
      case 1 => room.setTile(6, 22, new ClosedDoor(Coordinates(7 + rowOffset, 3 + colOffset))) // right
      case 2 => room.setTile(10, 12, new ClosedDoor(Coordinates(11 + rowOffset, 12 + colOffset))) // bottom
      case 3 => room.setTile(6, 2, new ClosedDoor(Coordinates(19 + rowOffset, 3 + colOffset))) // left
    }

    room
  }
}
