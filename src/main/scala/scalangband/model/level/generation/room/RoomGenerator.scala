package scalangband.model.level.generation.room

import scalangband.data.level.rooms.rectangle.BasicRectangularRoom
import scalangband.model.item.Armory
import scalangband.model.monster.Bestiary

import scala.util.Random

trait RoomGenerator {
  def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room
}
