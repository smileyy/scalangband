package scalangband.model.item.garbage

import scalangband.model.item.{Item, ItemGenerator}

import scala.util.Random

object GarbageGenerator extends ItemGenerator {
  override def generate(random: Random, depth: Int): Item = PotteryShard
}
