package scalangband.model.action.monster

import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.model.location.*
import scalangband.model.monster.Monster
import scalangband.model.{GameAccessor, GameCallback}

import scala.util.Random

object MonsterPassAction extends MonsterAction {
  override def apply(monster: Monster, game: GameAccessor, callback: GameCallback): Option[ActionResult] = None
}

object RandomMovementAction extends MonsterAction {
  override def apply(monster: Monster, accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    val direction: Direction = Random.nextInt(8) match {
      case 0 => Up
      case 1 => Down
      case 2 => Left
      case 3 => Right
      case 4 => UpLeft
      case 5 => UpRight
      case 6 => DownLeft
      case 7 => DownRight
    }
    
    callback.level.tryToMoveMonster(monster, direction)
    
    None
  }
}

class TauntAction(taunt: String) extends MonsterAction {
  override def apply(monster: Monster, accessor: GameAccessor, callback: GameCallback): Option[ActionResult] = {
    Some(MessagesResult(List(taunt)))
  }
}