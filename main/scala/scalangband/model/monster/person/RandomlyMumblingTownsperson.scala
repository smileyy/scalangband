package scalangband.model.monster.person

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.action.{GameAction, PassAction, PhysicalAction, RandomMovementAction, TauntAction}
import scalangband.model.location.Coordinates
import scalangband.model.monster.Monster
import scalangband.model.util.Weighted

class RandomlyMumblingTownsperson(coords: Coordinates) extends Monster("Randomly Mumbling Townsperson", 2, coordinates = coords) {
  override def weightedActions: Seq[Weighted[GameAction]] = Seq(
    Weighted(PassAction, 90),
    Weighted(RandomMovementAction(this), 9),
    Weighted(TauntAction("The townsperson mumbles incoherently"), 1)
  )
}