package scalangband.model.level.generation.room

import scalangband.model.level.generation.DungeonLevelCanvas
import scalangband.model.location.{Coordinates, Direction, DownDirection, LeftDirection, RightDirection, UpDirection}
import scalangband.model.tile.*

import scala.util.Random

class RoomBuilder(val top: Int, val left: Int, val width: Int, val height: Int, val depth: Int) { outer =>
  private var rows: List[RowBuilder] = List.empty
  private var attachmentPoints: Map[Direction, List[Coordinates]] = Map(
    UpDirection -> List.empty,
    DownDirection -> List.empty,
    LeftDirection -> List.empty,
    RightDirection -> List.empt)
  )

  private var currentRow = 0

  private def addAttachmentPoint(direction: Direction, column: Int): Unit = {
    val points: List[Coordinates] = attachmentPoints(direction)
    attachmentPoints = attachmentPoints + (direction -> (new Coordinates(top + currentRow, left + column) :: points))
  }

  def row(): RowBuilder = {
    new RowBuilder()
  }

  def build(): Room = {
    if (rows.size != height) {
      throw new Exception(s"Expected $height rows but was ${rows.size}")
    }

    new Room:
      override def top: Int = outer.top
      override def left: Int = outer.left
      override def height: Int = outer.height
      override def width: Int = outer.width
      override def depth: Int = outer.depth

      override def attachmentPoint(random: Random, direction: Direction): Coordinates = {
        val points = outer.attachmentPoints(direction)
        val index = random.nextInt(points.size)
        points(index)
      }

      override def addToLevel(random: Random, canvas: DungeonLevelCanvas): Unit = {
        var currentRow = 0
        var currentCol = 0

        rows.reverse.foreach( row =>
          currentCol = 0
          row.tiles.reverse.foreach( tile =>
            canvas.setTile(currentRow, currentCol, tile.create())
            tile.match {
              case MonsterFactory => canvas.addMonster(currentRow, currentCol, depth)
              case _ =>
            }
            currentCol = currentCol + 1
          )
          currentRow = currentRow + 1
        )
      }
  }

  class RowBuilder {
    var tiles: List[TileFactory] = List.empty
    private var currentColumn = 0

    private def add(tile: TileFactory): RowBuilder = {
      tiles = tile :: tiles
      currentColumn = currentColumn + 1
      this
    }

    def w: RowBuilder = {
      add(WallFactory)
    }

    def f: RowBuilder = {
      add(EmptyFloorFactory)
    }

    def d: RowBuilder = {
      add(DoorFactory)
    }

    def m: RowBuilder = {
      add(MonsterFactory)
    }

    def L: RowBuilder = {
      outer.addAttachmentPoint(LeftDirection, currentColumn)
      add(WallFactory)
    }

    def T: RowBuilder = {
      outer.addAttachmentPoint(UpDirection, currentColumn)
      add(WallFactory)
    }

    def R: RowBuilder = {
      outer.addAttachmentPoint(RightDirection, currentColumn)
      add(WallFactory)
    }

    def B: RowBuilder = {
      outer.addAttachmentPoint(DownDirection, currentColumn)
      add(WallFactory)
    }

    def build(): RoomBuilder = {
      if (tiles.size != outer.width) {
        throw new Exception(s"Expected ${outer.width} tiles but found ${tiles.size}")
      }

      outer.rows = this :: outer.rows
      outer.currentRow = outer.currentRow + 1

      RoomBuilder.this
    }
  }
}

trait TileFactory {
  def create(): Tile
}

object WallFactory extends TileFactory {
  override def create(): Tile = new RemovableWall()
}

object EmptyFloorFactory extends TileFactory {
  override def create(): Tile = Floor.empty()
}

object DoorFactory extends TileFactory {
  override def create(): Tile = new ClosedDoor()
}

object MonsterFactory extends TileFactory {
  override def create(): Tile = Floor.empty()
}

trait RoomBuilderGenerator extends RoomGenerator {
  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = {
    builder(top, left, height, width, depth).build()
  }

  def builder(top: Int, left: Int, height: Int, width: Int, depth: Int): RoomBuilder
  def height: Int
  def width: Int
}