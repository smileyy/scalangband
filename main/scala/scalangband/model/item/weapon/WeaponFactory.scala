package scalangband.model.item.weapon

import scalangband.model.util.DiceRoll

import scala.util.Random

trait WeaponFactory {
  def apply(random: Random, depth: Int): Weapon = {
    new Weapon(name, damage)
  }
  
  def name: String 
  def damage: DiceRoll
}

