package scalangband.model.tile

import scalangband.data.item.money.Money
import scalangband.model.item.Item
import scalangband.model.{Creature, Representable}

import scala.annotation.tailrec
import scala.collection.mutable

class Floor(occ: Option[Creature], val items: mutable.ListBuffer[Item]) extends OccupiableTile(occ) {
  def addItem(item: Item): Unit = {
    this.items += item
  }

  def addItems(items: List[Item]): Unit = {
    this.items ++= items
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