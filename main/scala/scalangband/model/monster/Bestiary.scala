package scalangband.model.monster

import scalangband.data.monster.ant.GiantWhiteAnt
import scalangband.data.monster.centipede.{GiantYellowCentipede, MetallicRedCentipede}
import scalangband.data.monster.mold.GreyMold
import scalangband.data.monster.mushroom.GreyMushroomPatch
import scalangband.model.location.Coordinates

import scala.util.Random

class Bestiary(
    factoriesByLevel: Map[Int, IndexedSeq[MonsterFactory]]) {
  def generateMonster(random: Random, depth: Int, coordinates: Coordinates): Option[Monster] = {
    factoriesByLevel.get(depth)
      .map(factories => factories(random.nextInt(factories.size)))
      .map(factory => factory(coordinates, random))
  }
}
object Bestiary {
  def apply(): Bestiary = apply(Seq(
    // Level 1
//    GiantYellowCentipede,
//    GreyMold,
    GreyMushroomPatch,

    // Level 2
//    GiantWhiteAnt,

    // Level 3
//    MetallicRedCentipede,
 ))

  def apply(factories: Seq[MonsterFactory]): Bestiary = {
    val map = factories.map(factory => (factory.spec.level, factory))
      .groupBy((level, _) => level)
      .map((level, seq) => (level, seq.map((_, factory) => factory).toIndexedSeq))
    new Bestiary(map)
  }
}
