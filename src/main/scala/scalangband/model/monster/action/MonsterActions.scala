package scalangband.model.monster.action

import org.slf4j.{Logger, LoggerFactory}
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.level.DungeonLevelAccessor
import scalangband.model.location.*
import scalangband.model.location.Direction.allDirections
import scalangband.model.monster.Monster
import scalangband.model.monster.attack.MeleeAttack
import scalangband.model.player.{Player, PlayerAccessor}
import scalangband.model.util.{BresenhamLine, Weighted}
import scalangband.model.{GameAccessor, GameCallback}

import scala.util.Random

class MonsterActions(adjacent: Seq[Weighted[MonsterAction]], los: Seq[Weighted[MonsterAction]], otherwise: Seq[Weighted[MonsterAction]]) {
  def select(monster: Monster, game: GameAccessor): MonsterAction = {
    if (isAdjacent(monster, game.player)) {
      Weighted.selectFrom(adjacent)
    } else if (hasLineOfSight(monster, game.player, game.level)) {
      Weighted.selectFrom(los)
    } else {
      Weighted.selectFrom(otherwise)
    }
  }

  private def isAdjacent(monster: Monster, player: PlayerAccessor) = {
    allDirections.exists(dir => monster.coordinates + dir == player.coordinates)
  }

  private def hasLineOfSight(monster: Monster, player: PlayerAccessor, level: DungeonLevelAccessor): Boolean = {
    new BresenhamLine(monster.coordinates, player.coordinates).find(coords => level.tile(coords).opaque) match {
      case Some(_) => false
      case None => true
    }
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
    val direction: Direction = Direction.randomDirection()
    Logger.debug(s"${monster.name} is trying to move $direction")
    callback.level.tryToMoveMonster(monster, direction)
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