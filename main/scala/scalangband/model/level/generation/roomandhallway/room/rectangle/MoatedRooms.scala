package scalangband.model.level.generation.roomandhallway.room.rectangle

import scalangband.model.level.generation.roomandhallway.room.rectangle.StandardMoatedRoom.{
  InteriorHeight,
  InteriorWidth
}
import scalangband.model.level.generation.roomandhallway.room.{Room, RoomGenerator}
import scalangband.model.level.generation.terrain.{
  CheckerboardTerrainGenerator,
  EmptyFloorTerrainGenerator,
  RectangularMoatTerrainGenerator
}

import scala.util.Random

object StandardMoatedRoom extends RoomGenerator {
  val InteriorHeight = 7
  val InteriorWidth = 19

  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = new BasicRectangularRoom(
    top,
    left,
    InteriorHeight + 8,
    InteriorWidth + 8,
    depth,
    EmptyFloorTerrainGenerator + new RectangularMoatTerrainGenerator()
  )
}

object CheckerboardMoatedRoom extends RoomGenerator {
  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = new BasicRectangularRoom(
    top,
    left,
    InteriorHeight + 8,
    InteriorWidth + 8,
    depth,
    EmptyFloorTerrainGenerator +
      new RectangularMoatTerrainGenerator(true) +
      new CheckerboardTerrainGenerator(2, 2, 7, 19)
  )
}
