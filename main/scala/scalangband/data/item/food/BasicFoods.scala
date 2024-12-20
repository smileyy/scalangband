package scalangband.data.item.food

import scalangband.bridge.rendering.TextColors.{LightRed, LightUmber}
import scalangband.model.item.food.{FoodFactory, FoodSpec, IncreaseSatietyBy}
import scalangband.model.item.{Item, ItemArchetype, ItemFactory, ItemQuality}

import scala.util.Random

object Apple extends FoodFactory {
  override def spec: FoodSpec = FoodSpec(
    name = "Apple",
    satiety = IncreaseSatietyBy(500),
    color = LightRed
  )

  override def levels: Range = 0 to 20
  override def commonness: Int = 40
}

object RationOfFood extends FoodFactory {
  override def spec: FoodSpec = FoodSpec(
    name = "Ration of Food",
    satiety = IncreaseSatietyBy(1500),
    color = LightUmber
  )

  override def levels: Range = 0 to 50
  override def commonness: Int = 40
}