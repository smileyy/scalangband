package scalangband.model.level.generation.room

import scalangband.model.level.generation.DungeonLevelCanvas
import scalangband.model.level.generation.monster.{MonsterGeneration, RandomMonsterGeneration}
import scalangband.model.level.generation.terrain.{EmptyFloorTerrainGenerator, TerrainGenerator}
import scalangband.model.location.{Coordinates, Direction}

import scala.util.Random

trait Room {

  def top: Int
  def left: Int

  def height: Int
  def width: Int

  def bottom: Int = top + height - 1
  def right: Int = left + width - 1

  def depth: Int

  def attachmentPoint(random: Random, direction: Direction): Coordinates

  def addToLevel(random: Random, canvas: DungeonLevelCanvas): Unit
}
