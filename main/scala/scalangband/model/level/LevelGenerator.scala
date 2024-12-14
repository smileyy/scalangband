package scalangband.model.level

import scalangband.model.monster.Bestiary

import scala.util.Random

trait LevelGenerator {
  def generateLevel(random: Random, depth: Int, bestiary: Bestiary): Level
}