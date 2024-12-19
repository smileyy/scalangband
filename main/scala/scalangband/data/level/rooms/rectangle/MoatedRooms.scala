package scalangband.data.level.rooms.rectangle

import scalangband.model.level.generation.room.{Room, RoomGenerator}
import StandardMoatedRoom.{
  InteriorHeight,
  InteriorWidth
}
import scalangband.model.level.generation.DungeonLevelCanvas
import scalangband.model.level.generation.terrain.{
  CheckerboardTerrainGenerator,
  EmptyFloorTerrainGenerator,
  RectangularMoatTerrainGenerator,
  TerrainGenerator
}
import scalangband.model.tile.{ClosedDoor, RemovableWall}

import scala.util.Random

object StandardMoatedRoom extends RoomGenerator {
  val InteriorHeight = 5
  val InteriorWidth = 17

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
      new CheckerboardTerrainGenerator(4, 4, InteriorHeight, InteriorWidth)
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
      canvas.drawHLine(canvas.height / 2, 4, InteriorWidth, () => new RemovableWall())
      canvas.drawVLine(4, canvas.width / 2, InteriorHeight, () => new RemovableWall())

      canvas.setTile(4, 3, new ClosedDoor())
      canvas.setTile(4, InteriorWidth + 4, new ClosedDoor())
      canvas.setTile(InteriorHeight + 3, 3, new ClosedDoor())
      canvas.setTile(InteriorHeight + 3, InteriorWidth + 4, new ClosedDoor())
    }
  }
}
