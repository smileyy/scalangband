package scalangband.model.monster

import scalangband.model.Game
import scalangband.model.action.{GameAction, PhysicalAction}
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.level.Level

class RandomlyMumblingTownsperson extends Monster("Randomly Mumbling Townsperson") {
  override def getAction(level: Level): GameAction = new MumbleAction()
}

private class MumbleAction extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = Some(MessageResult("The townsperson mumbles incoherently."))
}
