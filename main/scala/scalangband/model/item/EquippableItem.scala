package scalangband.model.item

import scalangband.model.player.StatBonuses
import scalangband.model.player.StatBonuses.NoBonus

trait EquippableItem extends Item {
  def baseArmorClass: Int = 0
  def toArmor: Int = 0

  def toHit: Int = 0
  def toDamage: Int = 0
 
  def statBonus: StatBonuses = NoBonus
  
  def onNextTurn(): Unit = {}
}