package scalangband.data.item.food

import scalangband.bridge.rendering.TextColors.{LightRed, LightUmber}
import scalangband.model.item.food.{FoodFactory, FoodSpec, IncreaseSatietyBy}
import scalangband.model.item.{Item, ItemArchetype, ItemFactory, ItemQuality}

import scala.util.Random

object Apple extends FoodFactory {
  override def spec: FoodSpec = FoodSpec(
    name = "Apple",
    color = LightRed,
    satiety = IncreaseSatietyBy(500)
  )

  override def levels: Range = 0 to 20
  override def commonness: Int = 40
}

object RationOfFood extends FoodFactory {
  override def spec: FoodSpec = FoodSpec(
    name = "Ration of Food",
    color = LightUmber,
    satiety = IncreaseSatietyBy(1500)
  )

  override def levels: Range = 0 to 50
  override def commonness: Int = 40
}