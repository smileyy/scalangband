package scalangband.model.level.generation.terrain

import scalangband.model.level.generation.roomandhallway.DungeonLevelCanvas
import scalangband.model.tile.{ClosedDoor, Floor, RemovableWall}

import scala.util.Random

class RectangularMoatTerrainGenerator(createDoor: Boolean = true) extends TerrainGenerator {
  override def generate(random: Random, canvas: DungeonLevelCanvas): Unit = {
    canvas.fillRect(1, 1, canvas.height - 2, canvas.width - 2, () => new RemovableWall())
    canvas.fillRect(2, 2, canvas.height - 4, canvas.width - 4, () => Floor.empty())

    if (createDoor) {
      random.nextInt(4) match {
        case 0 => canvas.setTile(1, canvas.width / 2, new ClosedDoor()) // top
        case 1 => canvas.setTile(canvas.height - 2, canvas.width / 2, new ClosedDoor()) // bottom
        case 2 => canvas.setTile(canvas.height / 2, 1, new ClosedDoor()) // left
        case 3 => canvas.setTile(canvas.height / 2, canvas.width - 2, new ClosedDoor()) // right
      }
    }
  }
}
