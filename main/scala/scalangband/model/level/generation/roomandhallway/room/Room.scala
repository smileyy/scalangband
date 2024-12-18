package scalangband.model.level.generation.roomandhallway.room

import org.slf4j.LoggerFactory
import scalangband.model.level.Levels
import scalangband.model.level.generation.roomandhallway.DungeonLevelCanvas
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.monster.{Bestiary, Monster}
import scalangband.model.tile.{OccupiableTile, Tile}
import scalangband.model.util.RandomUtils

import scala.util.Random

trait Room {
  def top: Int
  def left: Int

  def height: Int
  def width: Int

  def bottom: Int = top + height - 1
  def right: Int = left + width - 1

  def attachmentPoint(direction: Direction): Coordinates

  def paint(canvas: DungeonLevelCanvas): Unit
}