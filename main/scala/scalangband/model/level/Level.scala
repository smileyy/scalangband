package scalangband.model.level

import scalangband.model.{Creature, Player}
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.monster.Monster
import scalangband.model.scheduler.SchedulerQueue
import scalangband.model.tile.{OccupiableTile, Tile}

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
    this(coordinates).asInstanceOf[OccupiableTile].setOccupant(player)
  }
  
  def addMonster(monster: Monster): Unit = {
    this(monster.coordinates).asInstanceOf[OccupiableTile].setOccupant(monster)
  }
  
  def moveMonster(monster: Monster, direction: Direction): Unit = {
    val targetCoordinates: Coordinates = monster.coordinates + direction
    this(targetCoordinates) match {
      case ot: OccupiableTile if !ot.occupied => 
        ot.setOccupant(monster)
        this(monster.coordinates).asInstanceOf[OccupiableTile].clearOccupant()
        monster.coordinates = targetCoordinates
      case _ =>
    }
  }
  
  def makeEverythingInvisible(): Unit = {
    for (row <- 0 until height) {
      for (col <- 0 until width) {
        tiles(row)(col).setVisible(false)
      }
    }
  }

  /**
   * Replaces the tile at the given coordinates. Thus far used when opening / closing / breaking a door. Be careful if
   * used to replace a tile that a monster is standing on (or worse, the player!)
   */
  def replaceTile(coordinates: Coordinates, tile: Tile): Unit = {
    tiles(coordinates.row)(coordinates.col) = tile
  }
}