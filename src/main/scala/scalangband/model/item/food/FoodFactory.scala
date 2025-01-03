package scalangband.model.item.food

import scalangband.model.item.*

import scala.util.Random

trait FoodFactory extends StackableItemFactory {
  override def archetype: ItemArchetype = FoodArchetype

  override def apply(
      random: Random = new Random(),
      quality: ItemQuality = NormalQuality,
      quantity: Option[Int] = None
  ): Item =
    new Food(this, quantity.getOrElse(spec.quantity(random)))

  def spec: FoodSpec
}
