package scalangband.model.monster.action

import org.slf4j.{Logger, LoggerFactory}
import scalangband.bridge.actionresult.{ActionResult, DeathResult, MessageResult, NoResult}
import scalangband.model.location.*
import scalangband.model.location.Direction.allDirections
import scalangband.model.monster.Monster
import scalangband.model.monster.attack.MeleeAttack
import scalangband.model.util.Weighted
import scalangband.model.{GameAccessor, GameCallback}

import scala.util.Random

class MonsterActions(adjacent: Seq[Weighted[MonsterAction]], otherwise: Seq[Weighted[MonsterAction]]) {
  def select(monster: Monster, game: GameAccessor): MonsterAction = {
    val isAdjacent = allDirections.exists(dir => monster.coordinates + dir == game.player.coordinates)
    if (isAdjacent) Weighted.selectFrom(adjacent) else Weighted.selectFrom(otherwise)
  }
}

class MeleeAttacksAction(attacks: Seq[MeleeAttack]) extends MonsterAction {
  override def apply(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    attacks.foreach { attack =>
      results = attack.attack(monster, game, callback) ::: results
    }

    results
  }
}
object MeleeAttacksAction {
  def apply(attack: MeleeAttack): MonsterAction = new MeleeAttacksAction(Seq(attack))
  def apply(attacks: Seq[MeleeAttack]): MonsterAction = new MeleeAttacksAction(attacks)
}

object MonsterPassAction extends MonsterAction {
  override def apply(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = List(NoResult)
}

object RandomMovementAction extends MonsterAction {
  val Logger: Logger = LoggerFactory.getLogger(classOf[RandomMovementAction.type])

  override def apply(monster: Monster, accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    val direction: Direction = Random.nextInt(8) match {
      case 0 => UpDirection
      case 1 => DownDirection
      case 2 => LeftDirection
      case 3 => RightDirection
      case 4 => UpLeftDirection
      case 5 => UpRightDirection
      case 6 => DownLeftDirection
      case 7 => DownRightDirection
    }

    Logger.debug(s"${monster.name} is trying to move $direction")
    callback.level.tryToMoveMonster(monster, direction)
    
    List(NoResult)
  }
}

class SpeakAction(message: String) extends MonsterAction {
  override def apply(monster: Monster, accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    SpeakAction.Logger.debug(s"${monster.name} says \"$message\"")
    List(MessageResult(message))
  }
}
object SpeakAction {
  private val Logger = LoggerFactory.getLogger(classOf[SpeakAction])
}