package scalangband.model.monster

import org.slf4j.LoggerFactory
import scalangband.data.monster.ant.*
import scalangband.data.monster.bat.*
import scalangband.data.monster.bird.*
import scalangband.data.monster.centipede.*
import scalangband.data.monster.eye.FloatingEye
import scalangband.data.monster.ickything.*
import scalangband.data.monster.kobold.SmallKobold
import scalangband.data.monster.mold.*
import scalangband.data.monster.mushroom.*
import scalangband.data.monster.reptile.RockLizard
import scalangband.data.monster.rodent.GiantWhiteMouse
import scalangband.data.monster.snake.*
import scalangband.model.item.Armory
import scalangband.model.location.Coordinates
import scalangband.model.monster.Bestiary.Logger

import scala.util.Random

class Bestiary(factories: Seq[MonsterFactory], armory: Armory) {
  private val factoriesByLevel = factories.map(factory => (factory.spec.depth, factory))
    .groupBy((level, _) => level)
    .map((level, seq) => (level, seq.map((_, factory) => factory).toIndexedSeq))

  def generateMonster(random: Random, depth: Int, coordinates: Coordinates): Monster = {
    val factoriesForLevel = factoriesByLevel(depth)
    val factory: MonsterFactory = factoriesForLevel(random.nextInt(factoriesForLevel.size))
    val monster = factory(random, coordinates, armory)
    Logger.debug(s"Generated $monster")
    monster
  }
}
object Bestiary {
  private val Logger = LoggerFactory.getLogger(classOf[Bestiary])

  def apply(armory: Armory): Bestiary = new Bestiary(Seq(
    // these are in the same order as Angband's `monsters.txt`

    // Level 1
    GreyMold,
    GreyMushroomPatch,
    GiantYellowCentipede,
    GiantWhiteCentipede,
    WhiteIckyThing,
    ClearIckyThing,
    GiantWhiteMouse,
    LargeWhiteSnake,
    SmallKobold,
    FloatingEye,
    RockLizard,
    SoldierAnt,
    FruitBat,

    // Level 2
    Crow,

    // Level 3
    GiantWhiteAnt,
    MetallicRedCentipede,
 ), armory)
}
