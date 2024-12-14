package scalangband.model.level

import scalangband.model.monster.Bestiary

import scala.util.Random

class RandomWeightedLevelGenerator(weightedGenerators: Seq[(LevelGenerator, Int)]) extends LevelGenerator {

  private val totalWeights = weightedGenerators.map((_, weight) => weight).sum

  override def generateLevel(random: Random, depth: Int, bestiary: Bestiary): DungeonLevel = {
    selectLevelGenerator(random).generateLevel(random, depth, bestiary)
  }
  
  private def selectLevelGenerator(random: Random): LevelGenerator = {
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
