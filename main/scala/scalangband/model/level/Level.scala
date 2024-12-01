package scalangband.model.level

import scalangband.model.Creature
import scalangband.model.location.Coordinates
import scalangband.model.scheduler.SchedulerQueue
import scalangband.model.tile.{OccupiableTile, Tile}

class Level(val depth: Int, val tiles: Array[Array[Tile]]) {
  def height: Int = tiles.length
  def width: Int = tiles(0).length

  def apply(coordinates: Coordinates): Tile = apply(coordinates.row, coordinates.col)
  def apply(row: Int, col: Int): Tile = tiles(row)(col)

  val creatures: SchedulerQueue = {
    val queue = SchedulerQueue.empty()
    tiles.flatten.flatMap(_.occupant).foreach(creature => queue.insert(creature))
    println(queue)
    queue
  }

  def addCreature(coordinates: Coordinates, creature: Creature): Unit = {
    // TODO: check that the space isn't already occupied
    this(coordinates).asInstanceOf[OccupiableTile].setOccupant(creature)
    creatures.insert(creature)
    println(creatures)
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