package scalangband.model.level

import org.slf4j.LoggerFactory
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.Creature
import scalangband.model.item.Item
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.monster.{BashesDoors, Monster, OpensDoors}
import scalangband.model.player.Player
import scalangband.model.tile.*

import scala.util.Random

class DungeonLevel(val depth: Int, val tiles: Array[Array[Tile]]) extends Tiled {
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

  /** Tries to move the monster in the given direction. If the monster can't move in that direction, nothing happens
    */
  def tryToMoveMonster(monster: Monster, direction: Direction): List[ActionResult] = {
    val targetCoordinates: Coordinates = monster.coordinates + direction
    this(targetCoordinates) match {
      case ot: OccupiableTile if !ot.occupied =>
        moveOccupant(monster.coordinates, targetCoordinates)
        List.empty
      case door: ClosedDoor => monster.doors match {
        case OpensDoors =>
          replaceTile(targetCoordinates, new OpenDoor())
          List.empty
        case BashesDoors =>
          replaceTile(targetCoordinates, new BrokenDoor())
          List(MessageResult("You hear a door burst open!"))
        case _ => List.empty
      }
      case _ => List.empty
    }
  }

  def addItemToTile(coordinates: Coordinates, item: Item): ActionResult = {
    this(coordinates) match {
      case floor: Floor =>
        floor.addItem(item)
        NoResult
      case _ =>
        getAdjacentCoordinates(coordinates, tile => tile.isInstanceOf[Floor], 1) match {
          case x :: _ =>
            this(x).asInstanceOf[Floor].addItem(item)
            NoResult
          case Nil => MessageResult(s"$item disappears.")
        }
    }
  }

  def addItemsToTile(coordinates: Coordinates, items: Iterable[Item]): List[ActionResult] = {
    var results: List[ActionResult] = List.empty
    items.foreach(item => results = addItemToTile(coordinates, item) :: results)
    results
  }

  /** Replaces the tile at the given coordinates. Thus far used when opening / closing / breaking a door. Be careful if
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
    new Random()
      .shuffle(Direction.allDirections)
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

class DungeonLevelCallback(private val level: DungeonLevel) {
  def replaceTile(coordinates: Coordinates, tile: Tile): Unit = level.replaceTile(coordinates, tile)
  def addItemToTile(coordinates: Coordinates, item: Item): ActionResult = level.addItemToTile(coordinates, item)
  def addItemsToTile(coordinates: Coordinates, items: Iterable[Item]): List[ActionResult] =
    level.addItemsToTile(coordinates, items)
  def tryToMoveMonster(monster: Monster, direction: Direction): List[ActionResult] =
    level.tryToMoveMonster(monster, direction)
}
