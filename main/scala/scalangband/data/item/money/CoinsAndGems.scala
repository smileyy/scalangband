package scalangband.data.item.money

import scalangband.bridge.rendering.TextColors.{Copper, Umber}
import scalangband.model.item.*
import scalangband.model.util.DiceRoll

import scala.util.Random

object CopperCoins extends ItemFactory {
  override def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): Item =
    new Money("Copper Coins", DiceRoll("1d100+50").roll(), Umber, "copper coins")

  override val archetype: ItemArchetype = MoneyArchetype
  override val levels: Range = 1 to 100
  override val commonness: Int = 50
}
