package scalangband.model.level.generation

import scalangband.model.Legendarium
import scalangband.model.item.Armory
import scalangband.model.level.DungeonLevel
import scalangband.model.monster.Bestiary

import scala.util.Random

trait DungeonLevelGenerator {
  def generateLevel(random: Random, depth: Int, legendarium: Legendarium): DungeonLevel
}