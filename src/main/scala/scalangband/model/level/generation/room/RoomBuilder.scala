package scalangband.model.level.generation.room

import scalangband.model.level.generation.DungeonLevelCanvas
import scalangband.model.location.*
import scalangband.model.tile.*

import scala.util.Random

object RoomBuilder {
  def apply(data: String)(top: Int, left: Int, depth: Int): Room = {
    val rows = data.linesIterator.toList

    val width = rows.head.length
    val height = rows.length

    require(width > 0, "Rooms must have at least one column")
    require(height > 0, "Rooms must have at least one row")
    require(rows.forall(_.length == width), "All rows of a room must have the same width")

    var ws = List.empty[Coordinates]
    var fs = List.empty[Coordinates]
    var ds = List.empty[Coordinates]
    var ms = List.empty[Coordinates]
    var Ls = List.empty[Coordinates]
    var Rs = List.empty[Coordinates]
    var Ts = List.empty[Coordinates]
    var Bs = List.empty[Coordinates]

    for {
      (row, rowIndex) <- rows.zipWithIndex
      (char, columnIndex) <- row.zipWithIndex
    } {
      val coordinates = Coordinates(row = rowIndex, col = columnIndex)
      char match {
        case 'w' | '#' => ws ::= coordinates
        case 'f' | ' ' => fs ::= coordinates
        case 'd' => ds ::= coordinates
        case 'm' => ms ::= coordinates
        case 'L' => Ls ::= coordinates
        case 'R' => Rs ::= coordinates
        case 'T' => Ts ::= coordinates
        case 'B' => Bs ::= coordinates
        case other => throw new Exception(s"Encountered unexpected character '$other' in RoomBuilder")
      }
    }

    // note: without these aliases, Room's methods would name shadow the values and would do infinite recursion.
    val dataTop = top
    val dataLeft = left
    val dataHeight = height
    val dataWidth = width
    val dataDepth = depth

    new Room {
      override def top: Int = dataTop

      override def left: Int = dataLeft

      override def height: Int = dataHeight

      override def width: Int = dataWidth

      override def depth: Int = dataDepth

      override def attachmentPoint(random: Random, direction: Direction): Coordinates = {
        val attachmentPoints = direction match
          case UpDirection => Ts
          case DownDirection => Bs
          case LeftDirection => Ls
          case RightDirection => Rs
        val relativeCoordinate = attachmentPoints(random.nextInt(attachmentPoints.size))
        Coordinates(relativeCoordinate.row + top, relativeCoordinate.col + left)
      }

      override def addToLevel(random: Random, canvas: DungeonLevelCanvas): Unit = {
        fs.foreach(c => canvas.setTile(c.row, c.col, Floor.empty()))
        ws.foreach(c => canvas.setTile(c.row, c.col, new RemovableWall()))
        ds.foreach(c => canvas.setTile(c.row, c.col, new ClosedDoor()))
        ms.foreach(c => canvas.setTile(c.row, c.col, Floor.empty()))
        ms.foreach(c => canvas.addMonster(c.row, c.col, dataDepth))
        Ls.foreach(c => canvas.setTile(c.row, c.col, new RemovableWall()))
        Rs.foreach(c => canvas.setTile(c.row, c.col, new RemovableWall()))
        Ts.foreach(c => canvas.setTile(c.row, c.col, new RemovableWall()))
        Bs.foreach(c => canvas.setTile(c.row, c.col, new RemovableWall()))
      }
    }
  }
}
