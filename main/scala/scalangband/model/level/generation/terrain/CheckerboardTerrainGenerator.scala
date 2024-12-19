package scalangband.model.level.generation.terrain

import scalangband.model.level.generation.roomandhallway.DungeonLevelCanvas
import scalangband.model.tile.RemovableWall

import scala.util.Random

class CheckerboardTerrainGenerator(row: Int, col: Int, height: Int, width: Int) extends TerrainGenerator {
  override def generate(random: Random, canvas: DungeonLevelCanvas): Unit = {
    for (rowIdx <- row until row + height) {
      for (colIdx <- col until col + width) {
        if ((rowIdx % 2 == 1 && colIdx % 2 == 1) || (rowIdx % 2 == 0 && colIdx % 2 == 0)) {
          canvas.setTile(rowIdx, colIdx, new RemovableWall())
        }
      }
    }
  }
}
