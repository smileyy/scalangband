package scalangband.model.item.food

import scalangband.model.item.{FoodArchetype, StackableItem, ItemArchetype}

import scala.swing.Color

class Food(factory: FoodFactory, var count: Int) extends StackableItem {
  private val spec = factory.spec

  override def name: String = spec.name
  override def archetype: ItemArchetype = FoodArchetype

  override def quantity: Int = count
  override def increment(quantity: Int): Unit = count += quantity
  override def decrement(quantity: Int): Unit = count -= quantity

  def satiety: Satiety = spec.satiety
  def message: String = spec.message

  override def color: Color = spec.color

  override def singular: String = spec.singular
  override def plural: String = spec.plural

  def clone(quantity: Int): StackableItem = new Food(factory, quantity)
}
