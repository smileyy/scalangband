package scalangband.model.level.generation

import scalangband.model.item.Armory
import scalangband.model.level.DungeonLevel
import scalangband.model.level.generation.room.RoomAndHallwayGenerator
import scalangband.model.monster.Bestiary

import scala.util.Random

class RandomWeightedLevelGenerator(weightedGenerators: Seq[(DungeonLevelGenerator, Int)]) extends DungeonLevelGenerator {

  private val totalWeights = weightedGenerators.map((_, weight) => weight).sum

  override def generateLevel(random: Random, depth: Int, armory: Armory, bestiary: Bestiary): DungeonLevel = {
    selectLevelGenerator(random).generateLevel(random, depth, armory, bestiary)
  }
  
  private def selectLevelGenerator(random: Random): DungeonLevelGenerator = {
    var selection = random.nextInt(totalWeights)
    
    var result = weightedGenerators.head._1
    
    for ((generator, weight) <- weightedGenerators) {
      if (selection < weight) {
        result = generator
      } else {
        selection = selection - weight
      }
    } 
    
    result
  }
}
object RandomWeightedLevelGenerator {
  def apply(): RandomWeightedLevelGenerator = {
    new RandomWeightedLevelGenerator(Seq(
      (RoomAndHallwayGenerator(), 100)
    ))
  }
}
