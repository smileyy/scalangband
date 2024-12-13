package scalangband.model.item

import scalangband.model.player.StatBonus
import scalangband.model.player.StatBonus.NoBonus

trait EquippableItem extends Item {
  def baseArmorClass: Int = 0
  def toArmor: Int = 0

  def toHit: Int = 0
  def toDamage: Int = 0
 
  def statBonus: StatBonus = NoBonus
  
  def onNextTurn(): Unit = {}
}