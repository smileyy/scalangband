package scalangband.model.level.generation.room

import scala.util.Random

trait RoomGenerator {
  def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room
}
