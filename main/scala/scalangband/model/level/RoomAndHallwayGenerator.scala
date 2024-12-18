package scalangband.model.level

import scalangband.model.item.Armory
import scalangband.model.level.room.rectangle.*
import scalangband.model.level.room.{Room, RoomGenerator}
import scalangband.model.location.*
import scalangband.model.monster.Bestiary
import scalangband.model.tile.*
import scalangband.model.util.Weighted

import scala.util.Random

/** Generates a dungeon based on rooms and hallways. The type of room generated is based on a set of generators with
  * weighted values. Basically, we want normal rooms to show up most of the time, and "special" rooms less frequently.
  * This probably ought to be updated to be weighted by depth as well as you don't want a Greater Checkerboard Vault to
  * show up on level 1.
  *
  * The algorithm is inspired by https://roguebasin.com/index.php/Dungeon-Building_Algorithm, but starts in the upper
  * left hand corner of the map, generating successive rooms down and to the right. There's a bit of a "downward slope"
  * bias, but if you try enough times, you'll eventually get something in that upper right corner.
  */
class RoomAndHallwayGenerator(weightedGenerators: Seq[Weighted[RoomGenerator]]) extends DungeonLevelGenerator {
  override def generateLevel(random: Random, depth: Int, armory: Armory, bestiary: Bestiary): DungeonLevel = {
    val builder = DungeonLevelBuilder(random, armory, bestiary)

    val firstRoom = generateRoom(random, depth, random.nextInt(4) + 2, random.nextInt(8) + 2)
    applyRoom(builder, firstRoom)
    var rooms = List(firstRoom)

    var consecutiveFailures = 0
    while (consecutiveFailures < 50) {
      val startingRoom = rooms(random.nextInt(rooms.size))
      val (direction, start) = chooseDirectionAndStart(random, startingRoom)

      tryToCreateRoom(random, builder, depth, start, direction) match {
        case Some(room) =>
          rooms = room :: rooms
          applyRoom(builder, room)
          attachRoom(random, builder, room, start, direction)
          room.addMonsters(random, bestiary)
          consecutiveFailures = 0
        case None => consecutiveFailures = consecutiveFailures + 1
      }
    }

    builder.build(random, depth, tiles => new DungeonLevel(depth, tiles))
  }

  private def chooseDirectionAndStart(random: Random, room: Room): (Direction, Coordinates) = {
    random.nextInt(2) match {
      case 0 => (RightDirection, room.getAttachmentPoint(random, RightDirection))
      case 1 => (DownDirection, room.getAttachmentPoint(random, DownDirection))
    }
  }

  private def tryToCreateRoom(
      random: Random,
      builder: DungeonLevelBuilder,
      depth: Int,
      start: Coordinates,
      offsetDir: Direction
  ): Option[Room] = {
    val (rowOffset, colOffset) = offsetDir match {
      // TODO: Explain why these numbers lead to decent, if short, hallways
      case RightDirection => (-(random.nextInt(4) + 1), random.nextInt(8) + 4)
      case DownDirection  => (random.nextInt(8) + 4, -(random.nextInt(4) + 1))
    }

    val room = generateRoom(random, depth, start.row + rowOffset, start.col + colOffset)

    if (inLevelBound(room, builder) && doesNotOverwriteAnything(room, builder)) Some(room) else None
  }

  private def inLevelBound(room: Room, builder: DungeonLevelBuilder): Boolean = {
    (room.top > 1) && (room.right < builder.width - 1) && (room.bottom < builder.height - 1) && (room.left > 1)
  }

  private def doesNotOverwriteAnything(room: Room, builder: DungeonLevelBuilder): Boolean = {
    // TODO: A more Scala-idiomatic way of writing this without `return`?
    for (row <- room.top until room.bottom) {
      for (col <- room.left until room.right) {
        if (!builder(row, col).isInstanceOf[RemovableWall]) {
          return false
        }
      }
    }

    true
  }

  private def applyRoom(builder: DungeonLevelBuilder, room: Room): Unit = {
    for (rowIdx <- 0 until room.height) {
      for (colIdx <- 0 until room.width) {
        builder.setTile(room.top + rowIdx, room.left + colIdx, room(rowIdx, colIdx))
      }
    }
  }

  private def attachRoom(
      random: Random,
      builder: DungeonLevelBuilder,
      room: Room,
      start: Coordinates,
      startingDirection: Direction
  ): Unit = {
    startingDirection match {
      case RightDirection => drawRight(random, builder, room, start)
      case DownDirection  => drawDown(random, builder, room, start)
    }
  }

  private def drawRight(random: Random, builder: DungeonLevelBuilder, room: Room, start: Coordinates): Unit = {
    val end = room.getAttachmentPoint(random, LeftDirection)
    val dx = end.col - start.col
    val turnAt = start.col + (dx / 2)

    builder.setTile(start, randomDoorTile(random))

    for (i <- start.col + 1 to turnAt) {
      builder.setTile(start.row, i, Floor.empty())
    }

    val jogStart = if (start.row <= end.row) start.row else end.row
    val jogEnd = if (start.row <= end.row) end.row else start.row
    for (i <- jogStart to jogEnd) {
      builder.setTile(i, turnAt, Floor.empty())
    }

    for (i <- turnAt until end.col) {
      builder.setTile(end.row, i, Floor.empty())
    }

    builder.setTile(end, randomDoorTile(random))
  }

  private def drawDown(random: Random, builder: DungeonLevelBuilder, room: Room, start: Coordinates): Unit = {
    val end = room.getAttachmentPoint(random, UpDirection)
    val dy = end.row - start.row
    val turnAt = start.row + (dy / 2)

    builder.setTile(start, randomDoorTile(random))

    for (i <- start.row + 1 to turnAt) {
      builder.setTile(i, start.col, Floor.empty())
    }

    val jogStart = if (start.col <= end.col) start.col else end.col
    val jogEnd = if (start.col <= end.col) end.col else start.col
    for (i <- jogStart to jogEnd) {
      builder.setTile(turnAt, i, Floor.empty())
    }

    for (i <- turnAt until end.row) {
      builder.setTile(i, end.col, Floor.empty())
    }

    builder.setTile(end, randomDoorTile(random))
  }

  private def randomDoorTile(random: Random): Tile = {
    random.nextInt(100) match {
      case x if x < 75 => new ClosedDoor()
      case x if x < 95 => new OpenDoor()
      case _           => new BrokenDoor()
    }
  }

  private def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    selectRoomGenerator(random, depth).generateRoom(random, depth, rowOffset, colOffset)
  }

  private def selectRoomGenerator(random: Random, depth: Int): RoomGenerator = {
    Weighted.selectFrom(random, weightedGenerators)
  }
}
object RoomAndHallwayGenerator {
  def apply(): RoomAndHallwayGenerator = {
    new RoomAndHallwayGenerator(
      Seq(
        Weighted(100, RectangularRoomGenerator),
        Weighted(10, EmptyMoatedRoomGenerator),
        Weighted(2, CheckerboardMoatedRoomGenerator),
        Weighted(2, FourBoxesMoatedRoomGenerator)
      )
    )
  }
}
