package scalangband.model.monster

import scalangband.model.location.Coordinates
import scalangband.model.monster.ant.*
import scalangband.model.monster.centipede.*
import scalangband.model.monster.mold.*

import scala.util.Random

class Bestiary(
    factoriesByLevel: Map[Int, IndexedSeq[MonsterFactory]]) {
  def generateMonster(random: Random, depth: Int, coordinates: Coordinates): Option[Monster] = {
    factoriesByLevel.get(depth)
      .map(factories => factories(random.nextInt(factories.size)))
      .map(factory => factory(coordinates))
  }
}
object Bestiary {
  def apply(): Bestiary = apply(Seq(
    // Level 1
    GiantYellowCentipede,
    GreyMold,
    
    // Level 2
    GiantWhiteAnt,
    
    // Level 3
    MetallicRedCentipede,
 ))

  def apply(factories: Seq[MonsterFactory]): Bestiary = {
    val map = factories.map(factory => (factory.spec.depth, factory))
      .groupBy((level, _) => level)
      .map((level, seq) => (level, seq.map((_, factory) => factory).toIndexedSeq))
    new Bestiary(map)
  }
  
  
}
