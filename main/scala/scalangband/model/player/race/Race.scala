package scalangband.model.player.race

import scalangband.model.player.StatBonuses
import scalangband.model.util.DiceRoll

trait Race {
  def name: String

  def statBonus: StatBonuses

  def hitdice: DiceRoll

  def experienceFactor: Int
}
