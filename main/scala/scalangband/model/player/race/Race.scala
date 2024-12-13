package scalangband.model.player.race

import scalangband.model.player.StatBonus
import scalangband.model.util.DiceRoll

trait Race {
  def name: String
  
  def statBonus: StatBonus
  
  def hitdice: DiceRoll
}
