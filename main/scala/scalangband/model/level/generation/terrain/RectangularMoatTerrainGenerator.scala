package scalangband.model.level.generation.terrain

import scalangband.model.level.generation.DungeonLevelCanvas
import scalangband.model.tile.{ClosedDoor, Floor, RemovableWall}

import scala.util.Random

class RectangularMoatTerrainGenerator(createDoor: Boolean = true) extends TerrainGenerator {
  override def generate(random: Random, canvas: DungeonLevelCanvas): Unit = {
    canvas.fillRect(3, 3, canvas.height - 6, canvas.width - 6, () => new RemovableWall())
    canvas.fillRect(4, 4, canvas.height - 8, canvas.width - 8, () => Floor.empty())

    if (createDoor) {
      random.nextInt(4) match {
        case 0 => canvas.setTile(3, canvas.width / 2, new ClosedDoor()) // top
        case 1 => canvas.setTile(canvas.height - 4, canvas.width / 2, new ClosedDoor()) // bottom
        case 2 => canvas.setTile(canvas.height / 2, 3, new ClosedDoor()) // left
        case 3 => canvas.setTile(canvas.height / 2, canvas.width - 4, new ClosedDoor()) // right
      }
    }
  }
}
