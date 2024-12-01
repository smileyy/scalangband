package scalangband.model.scheduler

import scalangband.model.Creature

/**
 * A doubly linked list of creatures, used for the scheduling system. This acts like a priority queue, with the head of
 * the queue ebing the creature with the most energy. Inserting a new creature places the creature at the end then
 * bubbles it up to the appropriate place in the queue. This (probably?) has an advantage over a heap-based priority
 * queue in that poll is O(1) instead of O(log N). However, the real advantage comes in typical use where a creature
 * being re-enqueued will typically be coming in with a minimal amount of energy, most likely the least energy of all
 * other creatures in the queue, resulting in typically O(1) re-queue, with the worst case of O(N). A disadvantage is
 * that initial construction is something like O(N-squared) vs. O(N logN) for a priority queue.
 *
 * @param first the node containing the creature with the most energy. In general, this should not be read directly
 *              when returning results; instead use `getFirst`
 * @param last the
 */
class SchedulerQueue(private var first: SchedulerNode = null, private var last: SchedulerNode = null) {
  def peek: Creature = firstNode.creature

  def poll(): Creature = {
    val result = firstNode

    if (result == null) {
      throw IndexOutOfBoundsException("Queue is empty")
    }

    this.first = result.next

    result.creature
  }

  private def firstNode: SchedulerNode = {
    if (first == null) {
      // in practice this never happens; even if everything on the level is dead, the player will still be in the
      // queue whenever it is being polled. Still, I'd rather throw an Exception here, rather an an NPE elsewere.
      throw IndexOutOfBoundsException("Queue is empty")
    }

    first
  }

  def insert(creature: Creature): Unit = {
    val newNode = SchedulerNode(creature)

    if (first == null) {
      // if first is null, then last will be too
      first = newNode
      last = newNode
    } else {
      last.next = newNode
      newNode.prev = last
      last = newNode

      // bubble up
      while (newNode.prev != null && newNode.energy > newNode.prev.energy) {
        // swap
        val prev = newNode.prev
        val next = newNode.next

        if (next == null) {
          // node was at the end, need to reset last
          last = prev
        }

        prev.next = newNode.next
        newNode.prev = prev.prev
        newNode.next = prev

        if (prev.prev == null) {
          first = newNode
        } else {
          prev.prev.next = newNode
        }
      }
    }
  }

  override def toString: String = {
    if (first == null) "[]"
    else {
      var result = new StringBuilder("[")
      var node = first
      while (node != null) {
        result.append(node)
        if (node.next != null) result.append(", ")
        node = node.next
      }

      result.append("]")
      result.toString
    }
  }
}
object SchedulerQueue {
  def empty(): SchedulerQueue = new SchedulerQueue(null, null)
}

class SchedulerNode(var prev: SchedulerNode, var next: SchedulerNode, val creature: Creature) {
  def energy: Int = creature.energy
  override def toString: String = s"${creature.name}(${creature.energy})"
}
object SchedulerNode {
  def apply(creature: Creature): SchedulerNode = new SchedulerNode(null, null, creature)
}
