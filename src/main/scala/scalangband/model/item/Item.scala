package scalangband.model.item

import scalangband.model.Representable

import scala.swing.Color

trait Item extends Representable {
  def name: String
  def archetype: ItemArchetype
  def color: Color

  def singular: String

  override def toString: String = singular
} 

trait StackableItem extends Item {
  def quantity: Int
  def maxStackSize: Int = 40
  def stackSpace: Int = maxStackSize - quantity

  def stacksWith(item: Item): Boolean = {
    this.archetype == item.archetype && this.name == item.name
  }
  
  def increment(quantity: Int = 1): Unit
  def decrement(quantity: Int = 1): Unit

  def clone(quantity: Int): StackableItem
  
  def plural: String

  override def toString: String = {
    if (quantity == 1) singular else s"$quantity $plural"
  }
}
