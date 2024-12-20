package scalangband.model.util

import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.tile.Tile

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

object TileUtils {

  /** Returns an array of all the coordinates of the tiles that meet the given filter's criteria.
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

  def getAdjacentCoordinates(
      tiles: Array[Array[Tile]],
      start: Coordinates,
      accept: Tile => Boolean,
      count: Int
  ): Seq[Coordinates] = {
    var results: List[Coordinates] = List.empty
    val toSearch: mutable.Queue[Coordinates] = mutable.Queue(start)

    @tailrec
    def search(): Unit = {
      if (results.size < count && toSearch.nonEmpty) {
        val head = toSearch.dequeue()
        val neighboringCoordinates = Random.shuffle(Direction.allDirections).map(dir => head + dir)
        val neighboringTiles = neighboringCoordinates.map(c => tiles(c.row)(c.col))
        val tuples = neighboringCoordinates.zip(neighboringTiles)

        results = tuples.filter((_, tile) => accept(tile)).map(tuple => tuple._1).toList ::: results
        toSearch ++= tuples.filter((_, tile) => tile.passable).map(tuple => tuple._1)

        search()
      }
    }

    search()

    if (results.size > count) results.take(count) else results
  }
}
