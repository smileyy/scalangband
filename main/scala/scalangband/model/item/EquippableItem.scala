package scalangband.model.item

import scalangband.model.player.StatBonuses
import scalangband.model.player.StatBonuses.NoBonus

trait EquippableItem extends Item {
  def armorClass: Int = 0
  def toArmorBonus: Int = 0

  def toHit: Int = 0
  def toDamage: Int = 0
 
  def statBonus: StatBonuses = NoBonus
  
  def onNextTurn(): Unit = {}
}