package scalangband.model.level.room

import scalangband.model.monster.Bestiary

import scala.util.Random

trait RoomGenerator {
  def generateRoom(random: Random, depth: Int, rowOffset: Int, colOffset: Int): Room
}
