package scalangband.model.monster.person

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.action.{GameAction, PassAction, PhysicalAction, TauntAction}
import scalangband.model.monster.Monster
import scalangband.model.util.Weighted

class RandomlyMumblingTownsperson extends Monster("Randomly Mumbling Townsperson") {
  override def weightedActions: Seq[Weighted[GameAction]] = Seq(
    Weighted(PassAction, 90),
    Weighted(TauntAction("The townsperson mumbles incoherently"), 10)
  )
}