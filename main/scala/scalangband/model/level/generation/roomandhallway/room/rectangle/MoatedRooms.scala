package scalangband.model.level.generation.roomandhallway.room.rectangle

import scalangband.model.item.Armory
import scalangband.model.level.generation.roomandhallway.room.{Room, RoomGenerator}
import scalangband.model.level.generation.terrain.{EmptyFloorTerrainGenerator, RectangularMoatTerrainGenerator}
import scalangband.model.monster.Bestiary

import scala.util.Random

object StandardMoatedRoom extends RoomGenerator {
  private val InteriorHeight = 7
  private val InteriorWidth = 19

  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = {
    new BasicRectangularRoom(
      top,
      left,
      InteriorHeight + 8,
      InteriorWidth + 8,
      depth,
      EmptyFloorTerrainGenerator + new RectangularMoatTerrainGenerator()
    )
  }
}
