package scalangband.model.level.room.rectangle

import scalangband.model.level.room.{Room, RoomGenerator}
import scalangband.model.tile.{ClosedDoor, RemovableWall}

import scala.util.Random

object MoatedRectangularRoom {
  private val InteriorHeight = 7
  private val InteriorWidth = 19

  def apply(random: Random, rowOffset: Int, colOffset: Int, centeredDoor: Boolean = true): Room = {
    val room = RectangularRoom(rowOffset, colOffset, InteriorHeight + 6, InteriorWidth + 6)

    // add internal room
    for (rowIdx <- 2 until room.height - 2) {
      for (colIdx <- 2 until room.width - 2) {
        if (rowIdx == 2 || rowIdx == room.height - 3 || colIdx == 2 || colIdx == room.width - 3) {
          room.replaceTile(rowIdx, colIdx, coords => new RemovableWall(coords))
        }
      }
    }

    if (centeredDoor) {
      // add a door in the center of one of the walls
      random.nextInt(4) match {
        case 0 => room.replaceTile(2, 12, coords => new ClosedDoor(coords))  // top
        case 1 => room.replaceTile(6, 22, coords => new ClosedDoor(coords))  // right
        case 2 => room.replaceTile(10, 12, coords => new ClosedDoor(coords)) // bottom
        case 3 => room.replaceTile(6, 2, coords => new ClosedDoor(coords))   // left
      }
    }

    room
  }
}

object EmptyMoatedRoomGenerator extends RoomGenerator {
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    MoatedRectangularRoom(random, rowOffset, colOffset)
  }
}

object CheckerboardMoatedRoomGenerator extends RoomGenerator {
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val room = MoatedRectangularRoom(random, rowOffset, colOffset)

    for (rowIdx <- 3 until room.height - 3) {
      for (colIdx <- 3 until room.width - 3) {
        if ((rowIdx % 2 == 1 && colIdx % 2 == 1) || (rowIdx % 2 == 0 && colIdx % 2 == 0)) {
          room.replaceTile(rowIdx, colIdx, coords => new RemovableWall(coords))
        }
      }
    }

    room
  }
}

object FourBoxesMoatedRoomGenerator extends RoomGenerator {
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val room = MoatedRectangularRoom(random, rowOffset, colOffset, false)

    // draw vertical box dividing line
    for (rowIdx <- 3 until 10) {
      room.replaceTile(rowIdx, 12, coords => new RemovableWall(coords))
    }

    // draw horizontal box dividing line
    for (colIdx <- 3 until 22) {
      room.replaceTile(6, colIdx, coords => new RemovableWall(coords))
    }

    room.replaceTile(2, 2, coords => new ClosedDoor(coords))  // upper left corner
    room.replaceTile(2, 22, coords => new ClosedDoor(coords)) // upper right corner
    room.replaceTile(10, 22, coords => new ClosedDoor(coords))  // lower right corner
    room.replaceTile(10, 2, coords => new ClosedDoor(coords))  // lower left corner

    room

  }
}