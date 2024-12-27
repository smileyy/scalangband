package scalangband.model.player.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.item.food.Food
import scalangband.model.{GameAccessor, GameCallback}

class EatFoodAction(food: Food) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.eat(food)
  }
}
