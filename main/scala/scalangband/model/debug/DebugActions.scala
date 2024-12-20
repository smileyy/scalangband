package scalangband.model.debug

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.player.action.FreeAction
import scalangband.model.{GameAccessor, GameCallback}

object EnableDebugAction extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.enableDebug()
    List(MessageResult("Debug mode enabled"))
  }
}

object FullHealAction extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.fullHeal()
  }
}

object DebugLevelAction extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.debugLevel()
    List.empty
  }
}


object DisplaySatietyAction extends FreeAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    List(MessageResult(s"You have ${accessor.player.satiety} satiety."))
  }
}