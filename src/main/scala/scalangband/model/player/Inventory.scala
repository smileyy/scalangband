package scalangband.model.player

import scalangband.model.item.{Item, StackableItem}

import scala.collection.mutable

class Inventory(val items: mutable.IndexedBuffer[Item]) {
  def size: Int = items.size

  def addItem(item: Item): Unit = item match {
    case stackable: StackableItem =>
      val itemToAdd = stackable.clone(stackable.quantity)
      val stacks = items.filter(i => itemToAdd.stacksWith(i)).map(_.asInstanceOf[StackableItem])
      stacks.foreach { stack =>
        if (stack.stackSpace >= stackable.quantity) {
          stack.increment(stackable.quantity)
          itemToAdd.decrement(stackable.quantity)
        } else {
          val numberToAdd = stack.stackSpace
          stack.increment(numberToAdd)
          itemToAdd.decrement(numberToAdd)
        }
      }
      if (itemToAdd.quantity > 0) {
        items += itemToAdd
      }
    case _ => items += item
  }

  def getItem(idx: Int): Item = items(idx)

  def removeItem(item: Item): Unit = item match {
    case stackable: StackableItem =>
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

