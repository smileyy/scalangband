package scalangband.model.rooms

import scalangband.model.location.Coordinates
import scalangband.model.tile.RemovableWall

import scala.util.Random

class CheckerboardRoomGenerator extends RoomGenerator {
  private val delegate = new SurroundedByHallwayRoomGenerator()
  
  override def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    val room = delegate.generateEmptyRoom(random, rowOffset, colOffset)

    for (rowIdx <- 3 until room.height - 3) {
      for (colIdx <- 3 until room.width - 3) {
        if ((rowIdx % 2 == 1 && colIdx % 2 == 1) || (rowIdx % 2 == 0 && colIdx % 2 == 0)) {
          room.tiles(rowIdx)(colIdx) = new RemovableWall(Coordinates(rowIdx + rowOffset, colIdx + colOffset))
        }
      }
    }
    
    room
  }
}