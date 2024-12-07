package scalangband.model.monster

import scalangband.model.item.{Item, ItemGenerator}
import scalangband.model.util.Weighted
import scalangband.model.util.Weighted.selectFrom

import scala.util.Random

class MonsterInventoryGenerator(val minNumberOfItems: Int, val maxNumberOfItems: Int, val generators: Seq[Weighted[ItemGenerator]]) {
  def generate(random: Random, depth: Int): Seq[Item] = {
    (0 until random.nextInt(maxNumberOfItems - minNumberOfItems + 1) + minNumberOfItems).map(_ => selectFrom(generators).generate(random, depth))
  }
}
