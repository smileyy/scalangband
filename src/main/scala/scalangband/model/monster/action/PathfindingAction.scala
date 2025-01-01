package scalangband.model.monster.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.level.DungeonLevelAccessor
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.{GameAccessor, GameCallback}
import scalangband.model.monster.Monster
import scalangband.model.tile.{ClosedDoor, OccupiableTile, PermanentWall, RemovableWall, Tile, Wall}

import scala.annotation.tailrec
import scala.collection.mutable

object PathfindingAction extends MonsterAction {
  override def apply(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = {
    val path = new AStarPath(monster, game.player.coordinates, game.level)

    path.optimalDirection match {
      case Some(direction) => callback.level.tryToMoveMonster(monster, direction)
      case None            => List.empty
    }
  }
}

class AStarPath(monster: Monster, target: Coordinates, level: DungeonLevelAccessor) {
  private val heuristic = ChebyshevDistanceHeuristic

  def optimalDirection: Option[Direction] = {
    val start = PathNode(monster.coordinates, None, None, heuristic.score(monster.coordinates, target))
    val openSet = mutable.PriorityQueue(start)

    @tailrec
    def astar(visited: Set[Coordinates] = Set.empty): Option[Direction] = {
      if (openSet.isEmpty) None
      else
        openSet.dequeue() match {
          case p: PathNode if p.coordinates == target => p.firstMove
          case p: PathNode =>
            val nextNodes = Direction.allDirections
              .map(dir => (dir, p.coordinates + dir))
              .filter((_, coords) => !visited.contains(coords))
              .map((dir, coords) => (dir, coords, level.tile(coords)))
              .filter((_, coords, tile) => coords == target || canMoveTo(monster, tile))
              .map { (dir, coords, tile) =>
                PathNode(coords, Some(p), Some(dir), costToMove(monster, tile) + heuristic.score(coords, target))
              }

            nextNodes.foreach(node =>
              openSet.enqueue(node)
            )
            astar(visited ++ nextNodes.map(_.coordinates))
        }
    }

    astar(Set(start.coordinates))
  }

  def canMoveTo(monster: Monster, tile: Tile): Boolean = tile match {
    case _: PermanentWall                     => false
    case _: RemovableWall                     => false
    case _: ClosedDoor if monster.bashesDoors => true
    case _: ClosedDoor                        => false
    case ot: OccupiableTile if ot.occupied    => false
    case _                                    => true
  }

  def costToMove(monster: Monster, tile: Tile): Int = {
    1
  }
}

case class PathNode(coordinates: Coordinates, previous: Option[PathNode], direction: Option[Direction], priority: Int)
    extends Ordered[PathNode] {
  def firstMove: Option[Direction] = {
    @tailrec
    def firstMove(node: PathNode): Option[Direction] = {
      node.previous match {
        case None => None
        case Some(parent) => parent.previous match {
          case None => node.direction
          case _ => firstMove(parent)
        }
      }
    }

    firstMove(this)
  }

  override def compare(that: PathNode): Int = -Ordering.Int.compare(this.priority, that.priority)
}

object ChebyshevDistanceHeuristic {
  def score(start: Coordinates, end: Coordinates): Int = {
    math.max(math.abs(end.row - start.row), math.abs(end.col - start.col))
  }
}
