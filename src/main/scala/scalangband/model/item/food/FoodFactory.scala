package scalangband.model.item.food

import scalangband.model.item.*

import scala.util.Random

trait FoodFactory extends ItemFactory {
  override def archetype: ItemArchetype = FoodArchetype

  override def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): Item = new Food(spec)

  def spec: FoodSpec
}
