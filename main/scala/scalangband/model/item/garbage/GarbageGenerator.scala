package scalangband.model.item.garbage

import scalangband.model.item.{Item, ItemGenerator}

object GarbageGenerator extends ItemGenerator {
  override def generate(level: Int): Item = PotteryShard
}
