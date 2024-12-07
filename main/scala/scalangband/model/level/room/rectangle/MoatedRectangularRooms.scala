package scalangband.model.level.room.rectangle

import scalangband.model.level.room.{Room, RoomGenerator}
import scalangband.model.monster.Bestiary
import scalangband.model.tile.{ClosedDoor, RemovableWall}

import scala.util.Random

object MoatedRectangularRoom {
  private val InteriorHeight = 7
  private val InteriorWidth = 19

  def apply(random: Random, rowOffset: Int, colOffset: Int, effectiveDepth: Int, centeredDoor: Boolean = true): Room = {
    val room = RectangularRoom(rowOffset, colOffset, InteriorHeight + 6, InteriorWidth + 6, effectiveDepth)

    // add internal room
    for (rowIdx <- 2 until room.height - 2) {
      for (colIdx <- 2 until room.width - 2) {
        if (rowIdx == 2 || rowIdx == room.height - 3 || colIdx == 2 || colIdx == room.width - 3) {
          room.setTile(rowIdx, colIdx, new RemovableWall())
        }
      }
    }

    if (centeredDoor) {
      // add a door in the center of one of the walls
      random.nextInt(4) match {
        case 0 => room.setTile(2, 12, new ClosedDoor())  // top
        case 1 => room.setTile(6, 22, new ClosedDoor())  // right
        case 2 => room.setTile(10, 12, new ClosedDoor()) // bottom
        case 3 => room.setTile(6, 2, new ClosedDoor())   // left
      }
    }

    room
  }
}

object EmptyMoatedRoomGenerator extends RoomGenerator {
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    MoatedRectangularRoom(random, rowOffset, colOffset, depth)
  }
}

object CheckerboardMoatedRoomGenerator extends RoomGenerator {
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val room = MoatedRectangularRoom(random, rowOffset, colOffset, depth)

    for (rowIdx <- 3 until room.height - 3) {
      for (colIdx <- 3 until room.width - 3) {
        if ((rowIdx % 2 == 1 && colIdx % 2 == 1) || (rowIdx % 2 == 0 && colIdx % 2 == 0)) {
          room.setTile(rowIdx, colIdx, new RemovableWall())
        }
      }
    }

    room
  }
}

object FourBoxesMoatedRoomGenerator extends RoomGenerator {
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val room = MoatedRectangularRoom(random, rowOffset, colOffset, depth, false)

    // draw vertical box dividing line
    for (rowIdx <- 3 until 10) {
      room.setTile(rowIdx, 12, new RemovableWall())
    }

    // draw horizontal box dividing line
    for (colIdx <- 3 until 22) {
      room.setTile(6, colIdx, new RemovableWall())
    }

    room.setTile(2, 2, new ClosedDoor())  // upper left corner
    room.setTile(2, 22, new ClosedDoor()) // upper right corner
    room.setTile(10, 22, new ClosedDoor())  // lower right corner
    room.setTile(10, 2, new ClosedDoor())  // lower left corner

    room

  }
}