package scalangband.model.item.food

import scalangband.model.item.{FoodArchetype, Item, ItemArchetype}

import scala.swing.Color

class Food(spec: FoodSpec) extends Item {
  override def name: String = spec.name
  override def archetype: ItemArchetype = FoodArchetype

  override def color: Color = spec.color

  def satiety: Satiety = spec.satiety
}
object Food {
  val MaxSatiety: Int = 5000
}