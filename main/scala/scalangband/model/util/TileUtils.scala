package scalangband.model.util

import scalangband.model.location.Coordinates
import scalangband.model.tile.Tile

object TileUtils {
  /**
   * Returns an array of all the coordinates of the tiles that meet the given filter's criteria.
   */
  def allCoordinatesFor(tiles: Array[Array[Tile]], filter: Tile => Boolean): Array[Coordinates] = {
    var coordinates = List.empty[Coordinates]

    for (row <- tiles.indices) {
      for (col <- tiles(row).indices) {
        if (filter(tiles(row)(col))) coordinates = Coordinates(row, col) :: coordinates
      }
    }

    coordinates.toArray
  }
}
