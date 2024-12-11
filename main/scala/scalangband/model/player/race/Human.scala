package scalangband.model.player.race

import scalangband.model.util.DiceRoll

object Human extends Race {
  override def name: String = "Human"
  override def hitdice: DiceRoll = DiceRoll("1d9")
}
