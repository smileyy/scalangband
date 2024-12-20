package scalangband.model.item.food

import scalangband.model.item.{FoodArchetype, Item, ItemArchetype}

import scala.swing.Color

class Food(spec: FoodSpec) extends Item {
  override def name: String = spec.name
  override def archetype: ItemArchetype = FoodArchetype

  def satiety: Satiety = spec.satiety

  def message: String = spec.message
  override def color: Color = spec.color

}
