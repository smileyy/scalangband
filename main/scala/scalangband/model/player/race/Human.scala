package scalangband.model.player.race

import scalangband.model.player.StatBonus
import scalangband.model.player.StatBonus.NoBonus
import scalangband.model.util.DiceRoll

object Human extends Race {
  override def name: String = "Human"

  override def statBonus: StatBonus = NoBonus

  override def hitdice: DiceRoll = DiceRoll("1d9")
}
