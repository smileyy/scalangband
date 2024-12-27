package scalangband.model.player.race

import scalangband.model.player.StatBonuses
import scalangband.model.player.StatBonuses.NoBonus
import scalangband.model.util.DiceRoll

object Human extends Race {
  override def name: String = "Human"

  override def statBonus: StatBonuses = NoBonus

  override def hitdice: DiceRoll = DiceRoll("1d9")

  override def experienceFactor: Int = 100
}
