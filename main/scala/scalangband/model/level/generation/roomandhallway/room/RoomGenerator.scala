package scalangband.model.level.generation.roomandhallway.room

import scalangband.model.item.Armory
import scalangband.model.monster.Bestiary

import scala.util.Random

trait RoomGenerator {
  def generateRoom(random: Random, top: Int, left: Int, depth: Int, armory: Armory, bestiary: Bestiary): Room
}
