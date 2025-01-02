package scalangband.model.monster.action

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.level.DungeonLevelAccessor
import scalangband.model.location.{Coordinates, Direction}
import scalangband.model.monster.{CantOpenDoors, Monster}
import scalangband.model.tile.*
import scalangband.model.{GameAccessor, GameCallback}

import scala.annotation.tailrec
import scala.collection.mutable

object PathfindingAction extends MonsterAction {
  override def apply(monster: Monster, game: GameAccessor, callback: GameCallback): List[ActionResult] = {
    val path = new AStarPath(monster, game.player.coordinates, game.level)
    path.next match {
      case Some(direction) => callback.level.tryToMoveMonster(monster, direction)
      case None => MonsterPassAction.apply(monster, game, callback)
    }
  }
}

class AStarPath(monster: Monster, target: Coordinates, level: DungeonLevelAccessor) {
  def next: Option[Direction] = {
    val start = PathNode(monster.coordinates, None, None, monster.coordinates.chebyshevDistance(target))
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
                PathNode(coords, Some(p), Some(dir), coords.chebyshevDistance(target))
              }

            nextNodes.foreach(node => openSet.enqueue(node))
            astar(visited ++ nextNodes.map(_.coordinates))
        }
    }

    astar(Set(start.coordinates))
  }

  def canMoveTo(monster: Monster, tile: Tile): Boolean = tile match {
    case _: PermanentWall                                => false
    case _: RemovableWall                                => false
    case _: ClosedDoor if monster.doors == CantOpenDoors => false
    case _: ClosedDoor                                   => true
    case ot: OccupiableTile if ot.occupied               => false
    case _                                               => true
  }
}

case class PathNode(coordinates: Coordinates, previous: Option[PathNode], direction: Option[Direction], priority: Int)
    extends Ordered[PathNode] {
  def firstMove: Option[Direction] = {
    @tailrec
    def firstMove(node: PathNode): Option[Direction] = {
      node.previous match {
        case None => None
        case Some(parent) =>
          parent.previous match {
            case None => node.direction
            case _    => firstMove(parent)
          }
      }
    }

    firstMove(this)
  }

  override def compare(that: PathNode): Int = {
    // Scala priority queues are max heaps; we want the min to be at the head of the queue, so we negate the comparison.
    -this.priority.compareTo(that.priority)
  }
}