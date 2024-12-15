package scalangband.model.monster

import scalangband.data.monster.ant.*
import scalangband.data.monster.bat.*
import scalangband.data.monster.bird.*
import scalangband.data.monster.centipede.*
import scalangband.data.monster.ickything.*
import scalangband.data.monster.mold.*
import scalangband.data.monster.mushroom.*
import scalangband.data.monster.snake.*
import scalangband.model.location.Coordinates

import scala.util.Random

class Bestiary(factories: Seq[MonsterFactory]) {
  private val factoriesByLevel = factories.map(factory => (factory.spec.depth, factory))
    .groupBy((level, _) => level)
    .map((level, seq) => (level, seq.map((_, factory) => factory).toIndexedSeq))

  def generateMonster(random: Random, depth: Int, coordinates: Coordinates): Option[Monster] = {
    factoriesByLevel.get(depth)
      .map(factories => factories(random.nextInt(factories.size)))
      .map(factory => factory(coordinates, random))
  }
}
object Bestiary {
  def apply(): Bestiary = new Bestiary(Seq(
    // these are in the same order as Angband's `monsters.txt`

    // Level 1
    GreyMold,
    GreyMushroomPatch,
    GiantYellowCentipede,
    GiantWhiteCentipede,
    WhiteIckyThing,
    LargeWhiteSnake,
    SoldierAnt,
    FruitBat,


    // Level 2
    Crow,

    // Level 3
    GiantWhiteAnt,
    MetallicRedCentipede,
 ))
}
