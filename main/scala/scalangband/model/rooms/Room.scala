package scalangband.model.rooms

import scalangband.model.location.Coordinates
import scalangband.model.tile.Tile

class Room(val rowOffset: Int, val colOffset: Int, val tiles: Array[Array[Tile]]) {
  val height: Int = tiles.length
  val width: Int = tiles(0).length

  val top: Int = rowOffset
  val right: Int = colOffset + width - 1
  val bottom: Int = rowOffset + height - 1
  val left: Int = colOffset

  val upperLeftCorner: Coordinates = Coordinates(rowOffset, colOffset)
  val upperRightCorner: Coordinates = Coordinates(rowOffset, colOffset + width)
  val lowerRightCorner: Coordinates = Coordinates(rowOffset + height, colOffset + width)
  val lowerLeftCorner: Coordinates = Coordinates(rowOffset + height, colOffset)

  val allTiles: Array[Tile] = tiles.flatten

  val topWall: Array[Coordinates] = allTiles.map(_.coordinates).filter { c => c.rowIdx == rowOffset && c.colIdx != colOffset && c.colIdx != colOffset + width - 1 }
  val rightWall: Array[Coordinates] = allTiles.map(_.coordinates).filter { c => c.colIdx == colOffset + width -1 && c.rowIdx != rowOffset && c.rowIdx != rowOffset + height - 1 }
  val bottomWall: Array[Coordinates] = allTiles.map(_.coordinates).filter { c => c.rowIdx == rowOffset + height - 1 && c.colIdx != colOffset && c.colIdx != colOffset + width - 1 }
  val leftWall: Array[Coordinates] = allTiles.map(_.coordinates).filter { c => c.colIdx == colOffset && c.rowIdx != rowOffset && c.rowIdx != rowOffset + height - 1 }

  def getTile(rowIdx: Int, colIdx: Int): Tile = {
    tiles(rowIdx)(colIdx)
  }

  def setTile(rowIdx: Int, colIdx: Int, tile: Tile): Unit = {
    tiles(rowIdx)(colIdx) = tile
  }
}
