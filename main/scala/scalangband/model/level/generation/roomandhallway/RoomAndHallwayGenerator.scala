package scalangband.model.level.generation.roomandhallway

import scalangband.model.item.Armory
import scalangband.model.level.DungeonLevel
import scalangband.model.level.generation.DungeonLevelGenerator
import scalangband.model.level.generation.roomandhallway.RoomAndHallwayGenerator.FailureThreshold
import scalangband.model.level.generation.roomandhallway.room.rectangle.*
import scalangband.model.level.generation.roomandhallway.room.{Room, RoomGenerator}
import scalangband.model.location.Direction.randomCardinalDirection
import scalangband.model.location.*
import scalangband.model.monster.Bestiary
import scalangband.model.tile.{BrokenDoor, ClosedDoor, Floor, OpenDoor, RemovableWall, Tile}
import scalangband.model.util.Weighted

import scala.annotation.tailrec
import scala.util.Random

/** Generates a dungeon based on rooms and hallways. The type of room generated is based on a set of generators with
  * weighted values. Basically, we want normal rooms to show up most of the time, and "special" rooms less frequently.
  * This probably ought to be updated to be weighted by depth as well as you don't want a Greater Checkerboard Vault to
  * show up on level 1.
  *
  * The algorithm is inspired by https://roguebasin.com/index.php/Dungeon-Building_Algorithm.
  */
class RoomAndHallwayGenerator(roomGenerators: Seq[Weighted[RoomGenerator]]) extends DungeonLevelGenerator { outer =>
  override def generateLevel(random: Random, depth: Int, armory: Armory, bestiary: Bestiary): DungeonLevel = {
    new RoomAndHallwayGeneration(random, depth, armory, bestiary).generate()
  }

  private class RoomAndHallwayGeneration(
      random: Random,
      depth: Int,
      armory: Armory,
      bestiary: Bestiary
  ) {
    private val builder = DungeonLevelBuilder(random, armory, bestiary)

    def generate(): DungeonLevel = {
      addRoom()

      builder.build(random, depth, tiles => new DungeonLevel(depth, tiles))
    }

    @tailrec
    private def addRoom(rooms: List[Room] = List.empty, failures: Int = 0): Unit = {
      if (failures < FailureThreshold) {
        if (rooms.isEmpty) {
          placeFirstRoom() match {
            case Some(room) => addRoom(List(room))
            case None => addRoom()
          }
        } else {
          extend(rooms(random.nextInt(rooms.size)), randomCardinalDirection(random)) match {
            case Some(room) => addRoom(room :: rooms)
            case None => addRoom(rooms, failures + 1)
          }
        }
      }
    }

    private def placeFirstRoom(): Option[Room] = {
      tryToPlaceRoom(random.between(1, builder.height - 1), random.between(1, builder.width - 1))
    }

    private def extend(room: Room, direction: Direction): Option[Room] = {
      val start = room.attachmentPoint(random, direction)

      val (top, left) = direction match {
        case UpDirection => (start.row + random.between(-24, -12), start.col + random.between(-4, 4))
        case DownDirection => (start.row + random.between(6, 12), start.col + random.between(-4, 4))
        case LeftDirection => (start.row + random.between(-4, 4), start.col + random.between(-24, -12))
        case RightDirection => (start.row + random.between(-4, 4), start.col + random.between(6, 12))
      }

      val maybeRoom = tryToPlaceRoom(top, left)
      maybeRoom.foreach(room => connect(start, room.attachmentPoint(random, direction.opposite), direction))
      maybeRoom
    }

    private def tryToPlaceRoom(top: Int, left: Int): Option[Room] = {
      val room = generateNewRoom(top, left)

      if (isInLevel(room) && doesNotOverlap(room)) {
        // The canvas starts with the "inside" of the room, inside of the two-wall border of the room
        val canvas = builder.getCanvas(room.top + 2, room.left + 2, room.height - 4, room.width - 4)
        room.addTerrain(random, canvas)
        Some(room)
      } else None
    }

    private def isInLevel(room: Room): Boolean = {
      (room.top > 0) && (room.bottom < builder.height) && (room.left > 0) && (room.right < builder.width)
    }

    private def doesNotOverlap(room: Room): Boolean = {
      builder.slice(room.top, room.left, room.height, room.width)
        .flatten
        .forall(tile => tile.isInstanceOf[RemovableWall])
    }

    private def connect(start: Coordinates, end: Coordinates, direction: Direction): Unit = {
      builder.setTile(start, randomConnectionTile())

      direction match {
        case UpDirection => drawDown(end, start)
        case DownDirection => drawDown(start, end)
        case LeftDirection => drawRight(end, start)
        case RightDirection => drawRight(start, end)
      }

      builder.setTile(end, randomConnectionTile())
    }

    private def drawDown(start: Coordinates, end: Coordinates): Unit = {
      val dy = end.row - start.row
      val turnAt = start.row + (dy / 2)

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
    }

    private def drawRight(start: Coordinates, end: Coordinates): Unit = {
      val dx = end.col - start.col
      val turnAt = start.col + (dx / 2)

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
    }

    private def randomConnectionTile(): Tile = {
      random.nextInt(100) match {
        case x if x < 75 => new ClosedDoor()
        case x if x < 95 => new OpenDoor()
        case _           => new BrokenDoor()
      }
    }

    private def generateNewRoom(top: Int, left: Int) = {
      Weighted.selectFrom(random, roomGenerators).generateRoom(random, top, left, depth)
    }
  }
}
object RoomAndHallwayGenerator {
  private val FailureThreshold = 50

  def apply(): RoomAndHallwayGenerator = {
    new RoomAndHallwayGenerator(
      Seq(
        Weighted(100, RandomSizedRectangularRoom),
        Weighted(10, StandardMoatedRoom),
        Weighted(2, CheckerboardMoatedRoom),
        Weighted(2, FourBoxesMoatedRoom)
      )
    )
  }
}