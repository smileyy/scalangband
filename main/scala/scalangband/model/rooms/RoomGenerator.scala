package scalangband.model.rooms

import scala.util.Random

trait RoomGenerator {
  def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room
}
