package scalangband.model.player

import scalangband.model.item.{Item, StackableItem}

import scala.collection.mutable

class Inventory(val items: mutable.IndexedBuffer[Item]) {
  def size: Int = items.size

  def addItem(item: Item): Unit = item match {
    case stackable: StackableItem =>
      val stacks = items.filter(i => stackable.stacksWith(i)).map(_.asInstanceOf[StackableItem])
      stacks.foreach { stack =>
        if (stack.stackSpace >= stackable.quantity) {
          stack.increment(stackable.quantity)
          stackable.decrement(stackable.quantity)
        } else {
          val numberToAdd = stack.stackSpace
          stack.increment(numberToAdd)
          stackable.decrement(numberToAdd)
        }
      }
      if (stackable.quantity > 0) {
        items += stackable
      }
    case _ => items += item
  }

  def getItem(idx: Int): Item = items(idx)

  /**
   * We can make a couple of simplifying assumptions:
   *
   * (1) stacks are "greedy"; that is, if the max stack size is 40, and there are 44 of an item, we can guarantee that
   * (a) the stacks will be split 40 and 4 and (b) that the stack of 40 will appear before the stack of 4 in the
   * inventory list
   *
   * (2) you cannot remove more than a max stack size at once (this would be the case of dropping an item; consuming
   * an item will happen one at a time)
   */
  def removeItem(item: Item): Unit = item match {
    case stackable: StackableItem =>
      println(s"Removing $item")
      var stacksToRemove: List[Item] = List.empty
      var numberLeftToRemove = stackable.quantity

      val stacks = items.reverse.filter(i => stackable.stacksWith(i)).map(_.asInstanceOf[StackableItem])
      stacks.foreach { stack =>
        if (stack.quantity >= numberLeftToRemove) {
          stack.decrement(numberLeftToRemove)
          numberLeftToRemove = 0
        } else {
          val quantityToRemove = stack.quantity
          stack.decrement(quantityToRemove)
          numberLeftToRemove -= quantityToRemove
        }

        if (stack.quantity == 0) {
          stacksToRemove = stack :: stacksToRemove
        }

        stacksToRemove.foreach(toRemove => items -= toRemove)
      }
    case _ => items -= item
  }

  override def toString: String = items.map(_.name).mkString("(", ",", ")")
}
object Inventory {
  def empty(): Inventory = new Inventory(mutable.IndexedBuffer.empty)
}

