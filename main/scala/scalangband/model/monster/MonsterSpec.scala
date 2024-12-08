package scalangband.model.monster

import scalangband.model.Creature
import scalangband.model.action.monster.MonsterAction
import scalangband.model.item.Item
import scalangband.model.util.{DiceRoll, Weighted}

import scala.swing.Color
import scala.util.Random

/**
 * The idea of this type is that it will allow monster recall to be generated from it
 */
class MonsterSpec(val name: String, val archetype: MonsterArchetype, val depth: Int, val health: DiceRoll, val speed: Int = Creature.NormalSpeed, val evasion: Int, val actions: Seq[Weighted[MonsterAction]], val inventory: Option[MonsterInventoryGenerator] = None, val color: Color) {
  def generateStartingInventory(random: Random): Seq[Item] = inventory match {
    case Some(generator) => generator.generate(random, depth)
    case None => Seq.empty
  }
}
