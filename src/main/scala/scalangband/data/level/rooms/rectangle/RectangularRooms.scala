package scalangband.data.level.rooms.rectangle

import scalangband.model.item.Armory
import scalangband.model.level.generation.DungeonLevelCanvas
import scalangband.model.level.generation.monster.{MonsterGeneration, RandomMonsterGeneration}
import scalangband.model.level.generation.room.{Room, RoomGenerator}
import scalangband.model.level.generation.terrain.{EmptyFloorTerrainGenerator, TerrainGenerator}
import scalangband.model.location.*
import scalangband.model.monster.Bestiary
import scalangband.model.tile.Floor

import scala.util.Random

object RandomSizedRectangularRoom extends RoomGenerator {
  private val MinHeight = 8
  private val MaxHeight = 16

  private val MinWidth = 8
  private val MaxWidth = 24

  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = {
    val height = random.between(MinHeight, MaxHeight + 1)
    val width = random.between(MinWidth, MaxWidth + 1)
    new BasicRectangularRoom(top, left, height, width, depth)
  }
}

trait RectangularRoom extends Room {
  override def attachmentPoint(random: Random, direction: Direction): Coordinates = direction match {
    case UpDirection    => Coordinates(top + 1, random.between(left + 2, right - 2))
    case DownDirection  => Coordinates(bottom - 1, random.between(left + 2, right - 2))
    case LeftDirection  => Coordinates(random.between(top + 2, bottom - 2), left + 1)
    case RightDirection => Coordinates(random.between(top + 2, bottom - 2), right - 1)
  }

  override def addToLevel(random: Random, canvas: DungeonLevelCanvas): Unit = {
    addTerrain(random, canvas)
    addMonsters(random, canvas)
  }

  def terrain: TerrainGenerator = EmptyFloorTerrainGenerator
  def addTerrain(random: Random, canvas: DungeonLevelCanvas): Unit = {
    terrain.generate(random, canvas)
  }

  def monsters: MonsterGeneration = RandomMonsterGeneration
  def addMonsters(random: Random, canvas: DungeonLevelCanvas): Unit = {
    monsters.addMonsters(random, canvas, depth)
  }
}

class BasicRectangularRoom(
    val top: Int,
    val left: Int,
    val height: Int,
    val width: Int,
    val depth: Int,
    override val terrain: TerrainGenerator = EmptyFloorTerrainGenerator,
    override val monsters: MonsterGeneration = RandomMonsterGeneration
) extends RectangularRoom
