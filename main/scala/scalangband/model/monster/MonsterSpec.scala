package scalangband.model.monster

import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.action.monster.MonsterAction
import scalangband.model.action.player.PlayerAction
import scalangband.model.item.Item
import scalangband.model.util.{DiceRoll, Weighted}

import scala.swing.Color

/**
 * The idea of this type is that it will allow monster recall to be generated from it
 */
class MonsterSpec(val name: String, val archetype: MonsterArchetype, val level: Int, val health: DiceRoll, val baseSpeed: Int = BaseEnergyUnit, val actions: Seq[Weighted[MonsterAction]], val inventory: MonsterInventoryGenerator, val color: Color) {
  def generateStartingInventory(): Seq[Item] = inventory.generate(level)
}
