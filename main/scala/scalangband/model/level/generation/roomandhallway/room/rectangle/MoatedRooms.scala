package scalangband.model.level.generation.roomandhallway.room.rectangle

import scalangband.model.level.generation.roomandhallway.DungeonLevelCanvas
import scalangband.model.level.generation.roomandhallway.room.rectangle.StandardMoatedRoom.{InteriorHeight, InteriorWidth}
import scalangband.model.level.generation.roomandhallway.room.{Room, RoomGenerator}
import scalangband.model.level.generation.terrain.{CheckerboardTerrainGenerator, EmptyFloorTerrainGenerator, RectangularMoatTerrainGenerator, TerrainGenerator}
import scalangband.model.tile.{ClosedDoor, RemovableWall}

import scala.util.Random

object StandardMoatedRoom extends RoomGenerator {
  val InteriorHeight = 5
  val InteriorWidth = 11

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
      new CheckerboardTerrainGenerator(2, 2, 5, 11)
  )
}

object FourBoxesMoatedRoom extends RoomGenerator {
  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = new BasicRectangularRoom(
    top,
    left,
    InteriorHeight + 8,
    InteriorWidth + 8,
    depth,
    EmptyFloorTerrainGenerator +
      new RectangularMoatTerrainGenerator(false) +
      FourBoxesTerrainGenerator
  )

  object FourBoxesTerrainGenerator extends TerrainGenerator {
    override def generate(random: Random, canvas: DungeonLevelCanvas): Unit = {
      canvas.drawHLine(canvas.height / 2, 2, InteriorWidth, () => new RemovableWall())
      canvas.drawVLine(2, canvas.width / 2, InteriorHeight, () => new RemovableWall())

      canvas.setTile(2, 1, new ClosedDoor())
      canvas.setTile(2, InteriorWidth + 2, new ClosedDoor())
      canvas.setTile(InteriorHeight + 1, 1, new ClosedDoor())
      canvas.setTile(InteriorHeight + 1, InteriorWidth + 2, new ClosedDoor())
    }
  }
}
