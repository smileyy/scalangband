package scalangband.model.player.race

import scalangband.model.util.DiceRoll

trait Race {
  def name: String
  def hitdice: DiceRoll
}
