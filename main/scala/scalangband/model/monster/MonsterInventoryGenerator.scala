package scalangband.model.monster

import scalangband.model.item.{Item, ItemGenerator}
import scalangband.model.util.Weighted
import scalangband.model.util.Weighted.selectFrom

import scala.util.Random

class MonsterInventoryGenerator(val numberOfItems: Int, val generators: Seq[Weighted[ItemGenerator]]) {
  def generate(level: Int): Seq[Item] = {
    (0 until Random.nextInt(numberOfItems + 1)).map(_ => selectFrom(generators).generate(level))
  }
}
