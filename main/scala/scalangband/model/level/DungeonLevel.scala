package scalangband.model.level

import org.slf4j.LoggerFactory
import scalangband.model.Creature
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.monster.Monster
import scalangband.model.player.Player
import scalangband.model.tile.{Floor, OccupiableTile, Tile}

import scala.util.Random

class DungeonLevel(val depth: Int, val tiles: Array[Array[Tile]]) {
  var debug = false
  
  def height: Int = tiles.length
  def width: Int = tiles(0).length

  def apply(coordinates: Coordinates): Tile = apply(coordinates.row, coordinates.col)
  def apply(row: Int, col: Int): Tile = tiles(row)(col)

  def creatures: Seq[Creature] = tiles.flatten.flatMap(_.occupant)
  
  def startNextTurn(): Unit = {
    creatures.foreach { creature =>
      creature.onNextTurn()
    }
  }

  def addPlayer(coordinates: Coordinates, player: Player): Unit = {
    // TODO: check that the space isn't already occupied
    apply(coordinates).asInstanceOf[OccupiableTile].setOccupant(player)
    player.coordinates = coordinates
  }
  
  def moveOccupant(from: Coordinates, to: Coordinates): Unit = {
    val fromTile = apply(from).asInstanceOf[OccupiableTile]
    val toTile = apply(to).asInstanceOf[OccupiableTile]

    val occupant = fromTile.occupant.get

    fromTile.removeOccupant()
    toTile.setOccupant(occupant)

    occupant.coordinates = to
  }

  /**
   * Tries to move the monster in the given direction. If the monster can't move in that direction, nothing happens
   */
  def tryToMoveMonster(monster: Monster, direction: Direction): Unit = {
    val targetCoordinates: Coordinates = monster.coordinates + direction
    this(targetCoordinates) match {
      case ot: OccupiableTile if !ot.occupied => moveOccupant(monster.coordinates, targetCoordinates)
      case _ =>
    }
  }

  /**
   * Replaces the tile at the given coordinates. Thus far used when opening / closing / breaking a door. Be careful if
   * used to replace a tile that a monster is standing on (or worse, the player!)
   */
  def replaceTile(coordinates: Coordinates, tile: Tile): Unit = {
    tiles(coordinates.row)(coordinates.col) = tile
  }

  def setAllTilesInvisible(): Unit = forEachTile(tile => tile.setVisible(false))
  def setAllTilesVisible(): Unit = forEachTile(tile => tile.setVisible(true))

  private def forEachTile(f: Tile => Unit): Unit = {
    for (row <- 0 until height) {
      for (col <- 0 until width) {
        f(tiles(row)(col))
      }
    }
  }

  def emptyAdjacentFloorTileCoordinates(coordinates: Coordinates): Option[Coordinates] = {
    new Random().shuffle(Direction.allDirections)
      .map(dir => coordinates + dir)
      .find(coords => {
        val tile = apply(coords)
        tile.isInstanceOf[Floor] && !tile.occupied
      })
  }

}
object DungeonLevel {
  private val Logger = LoggerFactory.getLogger(classOf[DungeonLevel])
}

class DungeonLevelAccessor(private val level: DungeonLevel) {
  def depth: Int = level.depth
  def tile(coordinates: Coordinates): Tile = level(coordinates)
  def emptyAdjacentFloorTileCoordinates(coordinates: Coordinates): Option[Coordinates] = {
    level.emptyAdjacentFloorTileCoordinates(coordinates)
  }
}

class LevelCallback(private val level: DungeonLevel) {
  def replaceTile(coordinates: Coordinates, tile: Tile): Unit = level.replaceTile(coordinates, tile)
  def tryToMoveMonster(monster: Monster, direction: Direction): Unit = level.tryToMoveMonster(monster, direction)
}