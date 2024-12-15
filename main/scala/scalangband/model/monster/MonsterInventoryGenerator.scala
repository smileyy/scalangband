package scalangband.model.monster

import scalangband.model.item.{Item, ItemGenerator}
import scalangband.model.util.Weighted
import scalangband.model.util.Weighted.selectFrom

import scala.util.Random

class MonsterInventoryGenerator(probability: Int, val generators: Seq[Weighted[ItemGenerator]]) {
  def generate(random: Random, depth: Int): Option[Item] = {
    if (random.nextInt(100) < probability) {
      Some(selectFrom(generators).generate(random, depth))
    } else None
  }
}
