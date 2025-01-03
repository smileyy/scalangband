package scalangband.data.item.food

import scalangband.bridge.rendering.TextColors.{LightRed, LightUmber}
import scalangband.model.item.food.{FoodFactory, FoodSpec, IncreaseSatietyBy}
import scalangband.model.item.{Item, ItemArchetype, ItemFactory, ItemQuality}
import scalangband.model.util.{DiceRoll, Weighted}

import scala.util.Random

object Apple extends FoodFactory {
  override val spec: FoodSpec = FoodSpec(
    name = "Apple",
    singular = "an Apple",
    plural = "Apples",
    quantities = Seq(Weighted(90, DiceRoll("1d1")), Weighted(10, DiceRoll("1d2"))),
    satiety = IncreaseSatietyBy(500),
    color = LightRed
  )

  override val levels: Range = 0 to 20
  override val commonness: Int = 40
}

object RationOfFood extends FoodFactory {
  override val spec: FoodSpec = FoodSpec(
    name = "Ration of Food",
    singular = "a Ration of Food",
    plural = "Rations of Food",
    quantities = Seq(Weighted(100, DiceRoll("1d5"))),
    satiety = IncreaseSatietyBy(1500),
    color = LightUmber
  )

  override val levels: Range = 0 to 50
  override val commonness: Int = 40
}