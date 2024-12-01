package scalangband.model.action

import scalangband.model.Game
import scalangband.model.action.result.{ActionResult, MessageResult}
import scalangband.model.location.*
import scalangband.model.monster.Monster

import scala.util.Random

class RandomMovementAction(monster: Monster) extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = {
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
    
    game.level.moveMonster(monster, direction)
    
    None
  }

}

class TauntAction(taunt: String) extends PhysicalAction {
  override def apply(game: Game): Option[ActionResult] = {
    Some(MessageResult(taunt))
  }
}