package scalangband.data.item.money

import scalangband.model.item.{Item, ItemGenerator}

import scala.util.Random

object MoneyGenerator extends ItemGenerator {
  override def generate(random: Random, depth: Int): Item = {
    val amount = {
      if (depth == 0) random.nextInt(10 + 10)
      else random.nextInt(50) + 50 + depth
    }
    CopperCoins(amount)
  }
}
