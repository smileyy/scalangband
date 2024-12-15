package scalangband.model.debug

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.player.action.FreeAction

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
