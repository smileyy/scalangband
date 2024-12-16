package scalangband.model.item.weapon

import scalangband.model.item.ItemArchetype
import scalangband.model.util.DiceRoll

import scala.swing.Color

class WeaponSpec(val name: String, val archetype: ItemArchetype, val damage: DiceRoll, val color: Color)
