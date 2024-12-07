package scalangband.model.level

import org.slf4j.LoggerFactory
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.monster.Monster
import scalangband.model.tile.{OccupiableTile, Tile}
import scalangband.model.Creature
import scalangband.model.player.Player
import scalangband.model.util.CenteredRange

class Level(val depth: Int, val tiles: Array[Array[Tile]]) {
  def height: Int = tiles.length
  def width: Int = tiles(0).length

  def apply(coordinates: Coordinates): Tile = apply(coordinates.row, coordinates.col)
  def apply(row: Int, col: Int): Tile = tiles(row)(col)

  def creatures: Seq[Creature] = tiles.flatten.flatMap(_.occupant)
  
  def startNextTurn(): Unit = {
    creatures.foreach(_.startNextTurn())
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

    fromTile.clearOccupant()
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

  def setAllTilesVisible(): Unit = {
    for (row <- 0 until height) {
      for (col <- 0 until width) {
        tiles(row)(col).setVisible(true)
      }
    }
  }
}
object Level {
  private val Logger = LoggerFactory.getLogger(classOf[Level])
}

class LevelAccessor(private val level: Level) {
  def depth: Int = level.depth
  def tile(coordinates: Coordinates): Tile = level(coordinates)
}

class LevelCallback(private val level: Level) {
  def replaceTile(coordinates: Coordinates, tile: Tile): Unit = level.replaceTile(coordinates, tile)
  def tryToMoveMonster(monster: Monster, direction: Direction): Unit = level.tryToMoveMonster(monster, direction)
}

object LevelRanges {
  val ranges: Map[Int, CenteredRange] = Map(
    1 -> CenteredRange(1, 1 to 3),
    2 -> CenteredRange(2, 1 to 5)
  )
}