package scalangband.model.level

import scala.util.Random

class RandomWeightedLevelGenerator(generators: Seq[(LevelGenerator, Int)]) extends LevelGenerator {

  private val totalWeights = generators.map((_, weight) => weight).sum

  override def generateLevelWithoutStairs(random: Random, depth: Int): Level = {
    if (depth == 0) {
      TownGenerator.generateLevelWithoutStairs(random, depth)
    } else {
      selectLevelGenerator(random).generateLevelWithoutStairs(random, depth)
    }
  }
  
  private def selectLevelGenerator(random: Random): LevelGenerator = {
    var selection = random.nextInt(totalWeights)
    
    var result = generators.head._1
    
    for ((generator, weight) <- generators) {
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
