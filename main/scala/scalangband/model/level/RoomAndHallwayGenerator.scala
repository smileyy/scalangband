package scalangband.model.level

import scalangband.model.level.room.rectangle.*
import scalangband.model.level.room.{Room, RoomGenerator}
import scalangband.model.location.*
import scalangband.model.tile.*

import scala.util.Random

/**
 * Generates a dungeon based on rooms and hallways. The type of room generated is based on a set of generators with
 * weighted values. Basically, we want normal rooms to show up most of the time, and "special" rooms less frequently.
 * This probably ought to be updated to be weighted by depth as well as you don't want a Greater Checkerboard Value to
 * show up on level 1.
 *
 * The algorithm in use is inspired by https://roguebasin.com/index.php/Dungeon-Building_Algorithm, but starts in the
 * upper left hand corner of the map, generating successive rooms down and to the right.
 */
class RoomAndHallwayGenerator(weightedGenerators: Seq[(RoomGenerator, Int)]) extends LevelGenerator {
  private val totalWeights = weightedGenerators.map(_._2).sum

  override def generateLevelWithoutStairs(random: Random, depth: Int): Level = {
    val level = LevelGenerator.generateRandomlySizedWallFilledLevel(random, depth)

    val firstRoom = generateRoom(random, depth, random.nextInt(4) + 2, random.nextInt(8) + 2)
    applyRoom(level, firstRoom)
    var rooms = List(firstRoom)

    var consecutiveFailures = 0
    while (consecutiveFailures < 50) {
      val startingRoom = rooms(random.nextInt(rooms.size))
      val (direction, start) = chooseDirectionAndStart(random, startingRoom)

      tryToCreateRoom(random, level, start, direction) match {
        case Some(room) =>
          rooms = room :: rooms
          applyRoom(level, room)
          attachRoom(random, level, room, start, direction)
          consecutiveFailures = 0
        case None => consecutiveFailures = consecutiveFailures + 1
      }
    }

    level
  }

  private def chooseDirectionAndStart(random: Random, room: Room): (Direction, Coordinates) = {
    random.nextInt(2) match {
      case 0 => (Right, room.getAttachmentPoint(random, Right))
      case 1 => (Down, room.getAttachmentPoint(random, Down))
    }
  }

  private def tryToCreateRoom(random: Random, level: Level, start: Coordinates, dir: Direction): Option[Room] = {
    val (rowOffset, colOffset) = dir match {
      case Right => (-(random.nextInt(4) + 1), random.nextInt(8) + 4)
      case Down => (random.nextInt(8) + 4, -(random.nextInt(4) + 1))
    }

    val room = generateRoom(random, level.depth, start.rowIdx + rowOffset, start.colIdx + colOffset)

    if (inLevelBound(room, level) && doesNotOverlap(room, level)) Some(room) else None
  }

  private def inLevelBound(room: Room, level: Level): Boolean = {
    (room.top > 1) && (room.right < level.width - 1) && (room.bottom < level.height - 1) && (room.left > 1)
  }

  private def doesNotOverlap(room: Room, level: Level): Boolean = {
    room.tiles.flatten.map(_.coordinates).forall(coordinates => level(coordinates).isInstanceOf[RemovableWall])
  }

  private def applyRoom(level: Level, room: Room): Unit = {
    for (rowIdx <- 0 until room.height) {
      for (colIdx <- 0 until room.width) {
        level.replaceTile(Coordinates(room.top + rowIdx, room.left + colIdx), room(rowIdx, colIdx))
      }
    }
  }

  private def attachRoom(random: Random, level: Level, room: Room, start: Coordinates, startingDirection: Direction): Unit = {
    startingDirection match {
      case Right => drawRight(random, level, room, start)
      case Down => drawDown(random, level, room, start)
    }
  }

  private def drawRight(random: Random, level: Level, room: Room, start: Coordinates): Unit = {
    val end = room.getAttachmentPoint(random, Left)
    val dx = end.colIdx - start.colIdx
    val turnAt = start.colIdx + (dx / 2)

    level.replaceTile(start, randomDoorTile(random, start))

    for (i <- start.colIdx + 1 to turnAt) {
      val coordinates = Coordinates(start.rowIdx, i)
      level.replaceTile(coordinates, Floor.empty(coordinates))
    }

    val jogStart = if (start.rowIdx <= end.rowIdx) start.rowIdx else end.rowIdx
    val jogEnd = if (start.rowIdx <= end.rowIdx) end.rowIdx else start.rowIdx
    for (i <- jogStart to jogEnd) {
      val coordinates = Coordinates(i, turnAt)
      level.replaceTile(coordinates, Floor.empty(coordinates))
    }

    for (i <- turnAt until end.colIdx) {
      val coordinates = Coordinates(end.rowIdx, i)
      level.replaceTile(coordinates, Floor.empty(coordinates))
    }

    level.replaceTile(end, randomDoorTile(random, end))
  }

  private def drawDown(random: Random, level: Level, room: Room, start: Coordinates): Unit = {
    val end = room.getAttachmentPoint(random, Up)
    val dy = end.rowIdx - start.rowIdx
    val turnAt = start.rowIdx + (dy / 2)

    level.replaceTile(start, randomDoorTile(random, start))

    for (i <- start.rowIdx + 1 to turnAt) {
      val coordinates = Coordinates(i, start.colIdx)
      level.replaceTile(coordinates, Floor.empty(coordinates))
    }

    val jogStart = if (start.colIdx <= end.colIdx) start.colIdx else end.colIdx
    val jogEnd = if (start.colIdx <= end.colIdx) end.colIdx else start.colIdx
    for (i <- jogStart to jogEnd) {
      val coordinates = Coordinates(turnAt, i)
      level.replaceTile(coordinates, Floor.empty(coordinates))
    }

    for (i <- turnAt until end.rowIdx) {
      val coordinates = Coordinates(i, end.colIdx)
      level.replaceTile(coordinates, Floor.empty(coordinates))
    }

    level.replaceTile(end, randomDoorTile(random, end))
  }

  def randomDoorTile(random: Random, coordinates: Coordinates): Tile = {
    random.nextInt(100) match {
      case x if x < 75 => new ClosedDoor(coordinates)
      case x if x < 95 => new OpenDoor(coordinates, None)
      case _ => new BrokenDoor(coordinates, None)
    }
  }

  private def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room = {
    selectRoomGenerator(random, depth).generateRoom(random, depth, rowOffset, colOffset)
  }

  private def selectRoomGenerator(random: Random, depth: Int): RoomGenerator = {
    var selection = random.nextInt(totalWeights)

    var result = weightedGenerators.head._1

    for ((generator, weight) <- weightedGenerators) {
      if (selection < weight) {
        result = generator
      } else {
        selection = selection - weight
      }
    }

    result
  }
}
object RoomAndHallwayGenerator {
  def apply(): RoomAndHallwayGenerator = {
    new RoomAndHallwayGenerator(Seq(
      (RectangularRoomGenerator, 100),
      (EmptyMoatedRoomGenerator, 10),
      (CheckerboardMoatedRoomGenerator, 2),
      (FourBoxesMoatedRoomGenerator, 2),
    ))
  }
}