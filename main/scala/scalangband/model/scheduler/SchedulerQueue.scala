package scalangband.model.scheduler

import scalangband.model.Creature
import scalangband.model.monster.Monster

/**
 * A doubly linked list of creatures, used for the scheduling system. This acts like a priority queue, with the head of
 * the queue ebing the creature with the most energy. Inserting a new creature places the creature at the end then
 * bubbles it up to the appropriate place in the queue. This (probably?) has an advantage over a heap-based priority
 * queue in that poll is O(1) instead of O(log N). However, the real advantage comes in typical use where a creature
 * being re-enqueued will typically be coming in with a minimal amount of energy, most likely the least energy of all
 * other creatures in the queue, resulting in typically O(1) re-queue, with the worst case of O(N). A disadvantage is
 * that initial construction is something like O(N-squared) vs. O(N logN) for a priority queue.
 *
 * @param head the node containing the creature with the most energy. In general, this should not be read directly
 *              when returning results; instead use `getFirst`
 * @param last the node containing the creature with the least energy
 */
class SchedulerQueue(private var head: SchedulerNode = null, private var last: SchedulerNode = null) {
  def peek: Creature = head.creature

  def poll(): Creature = {
    val result = head
    
    this.head = result.next

    result.creature
  }
  
  def insert(creature: Creature): Unit = {
    val newNode = SchedulerNode(creature)

    if (head == null) {
      // if first is null, then last will be too
      head = newNode
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
          head = newNode
        } else {
          prev.prev.next = newNode
        }
      }
    }
  }
  
  def remove(monster: Monster): Unit = {
    var node = head
    
    var done = false
    while (node != null && done) {
      println(node.creature)
      if (node.creature == monster) {
        
        val prev = node.prev
        if (prev == null) {
          head = node.next
          head.prev = null
        } else {
          prev.next = node.next
          node.next.prev = prev
        }
        
        done = true
      }
    }
    
    node.prev = null
    node.next = null
  }

  override def toString: String = {
    if (head == null) "[]"
    else {
      val result = new StringBuilder("[")
      var node = head
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
  def apply(creatures: Iterable[Creature]): SchedulerQueue = {
    val queue = new SchedulerQueue(null, null)
    creatures.foreach(creature => queue.insert(creature))
    queue
  }
}

private class SchedulerNode(var prev: SchedulerNode, var next: SchedulerNode, val creature: Creature) {
  def energy: Int = creature.energy
  override def toString: String = s"${creature.name}(${creature.energy})"
}
object SchedulerNode {
  def apply(creature: Creature): SchedulerNode = new SchedulerNode(null, null, creature)
}
