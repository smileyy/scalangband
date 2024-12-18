package scalangband.model.level.generation.roomandhallway.room.rectangle

import scalangband.model.item.Armory
import scalangband.model.level.generation.roomandhallway.DungeonLevelCanvas
import scalangband.model.level.generation.roomandhallway.room.{Room, RoomGenerator}
import scalangband.model.location.{Coordinates, Direction, DownDirection, LeftDirection, RightDirection, UpDirection}
import scalangband.model.monster.Bestiary
import scalangband.model.tile.Floor

import scala.util.Random

object RectangularRoomGenerator extends RoomGenerator {
  private val MinHeight = 6
  private val MaxHeight = 16

  private val MinWidth = 6
  private val MaxWidth = 24

  override def generateRoom(
      random: Random,
      top: Int,
      left: Int,
      depth: Int,
      armory: Armory,
      bestiary: Bestiary
  ): Room = {
    val height = random.between(MinHeight, MaxHeight + 1)
    val width = random.between(MinWidth, MaxWidth + 1)
    new RectangularRoom(random, top, left, height, width, depth, armory, bestiary)
  }
}

class RectangularRoom(
    random: Random,
    val top: Int,
    val left: Int,
    val height: Int,
    val width: Int,
    depth: Int,
    armory: Armory,
    bestiary: Bestiary
) extends Room {
  override def attachmentPoint(direction: Direction): Coordinates = direction match {
    case UpDirection => Coordinates(top, random.between(left + 1, right - 1))
    case DownDirection => Coordinates(bottom, random.between(left + 1, right - 1))
    case LeftDirection => Coordinates(random.between(top + 1, bottom - 1), left)
    case RightDirection => Coordinates(random.between(top + 1, bottom -1 ), right)
  }

  override def paint(canvas: DungeonLevelCanvas): Unit = {
    canvas.fill(1, 1, height - 1, width - 1, () => Floor.empty())
  }
}
