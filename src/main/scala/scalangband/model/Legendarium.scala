package scalangband.model

import scalangband.model.item.{Armory, Item, ItemArchetype}
import scalangband.model.level.DungeonLevels
import scalangband.model.location.Coordinates
import scalangband.model.monster.{Bestiary, Monster, MonsterArchetype, MonsterFactory}

import scala.util.Random

class Legendarium(val armory: Armory, val bestiary: Bestiary) {
  def generateItem(random: Random, depth: Int): Item = armory.generateItem(random, depth)
  def generateItem(random: Random, archetype: ItemArchetype, depth: Int): Item =
    armory.generateItem(random, archetype, depth)

  def getMonsterFactory(random: Random, depth: Int): MonsterFactory = bestiary.getMonsterFactory(random, depth)

  def getMonsterFactory(random: Random, archetype: MonsterArchetype, depth: Int): Option[MonsterFactory] =
    bestiary.getMonsterFactory(random, archetype, depth)
  def generateMonster(random: Random, depth: Int, coordinates: Coordinates): Monster =
    bestiary.generateMonster(random, depth, coordinates, armory)
}
object Legendarium {
  def apply(): Legendarium = {
    new Legendarium(Armory(), Bestiary())
  }
}
