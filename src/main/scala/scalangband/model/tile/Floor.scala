package scalangband.model.tile

import scalangband.data.item.money.Money
import scalangband.model.item.{Item, StackableItem}
import scalangband.model.{Creature, Representable}

import scala.annotation.tailrec
import scala.collection.mutable

class Floor(occ: Option[Creature], val items: mutable.ListBuffer[Item]) extends OccupiableTile(occ) {
  def addItem(item: Item): Unit = item match {
    case stackable: StackableItem =>
      val itemToPlace = stackable.clone(stackable.quantity)
      val stacks = items.filter(i => itemToPlace.stacksWith(i)).map(_.asInstanceOf[StackableItem])
      stacks.foreach { stack =>
        if (stack.stackSpace >= stackable.quantity) {
          stack.increment(stackable.quantity)
          itemToPlace.decrement(stackable.quantity)
        } else {
          val numberToAdd = stack.stackSpace
          stack.increment(numberToAdd)
          itemToPlace.decrement(numberToAdd)
        }
      }
      if (itemToPlace.quantity > 0) {
        items += itemToPlace
      }
    case _ => items += item
  }

  def removeItem(item: Item): Unit = {
    items -= item
  }
  
  override def representation: Seq[Representable] = {
    val occupantRepresentation = occupant
    val itemRepresentation = if (items.size > 1) Some(PileOfItems) else if (items.size == 1) Some(items.head) else None
    val floorRepresentation = Some(this)

    (occupantRepresentation :: itemRepresentation :: floorRepresentation :: Nil).flatten
  }
  
  def money: Seq[Money] = {
    @tailrec
    def selectMoney(items: List[Item], accumulator: List[Money] = List.empty): List[Money] = {
      items match {
        case x :: xs => x match {
            case m: Money => selectMoney(xs, m :: accumulator)
            case _ =>  selectMoney(xs, accumulator)
          }
        case Nil => accumulator
      }
    }
  
    selectMoney(items.toList)
  }
}
object Floor {
  def empty() = new Floor(None, mutable.ListBuffer.empty[Item])
}

object PileOfItems extends Representable