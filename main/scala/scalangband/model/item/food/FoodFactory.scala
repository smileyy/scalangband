package scalangband.model.item.food

import scalangband.model.item.{FoodArchetype, Item, ItemArchetype, ItemFactory, ItemQuality, NormalQuality}

import scala.util.Random

trait FoodFactory extends ItemFactory {
  override def archetype: ItemArchetype = FoodArchetype

  override def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): Item = new Food(spec)
  
  def spec: FoodSpec
}
