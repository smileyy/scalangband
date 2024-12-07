package scalangband.model.item

import scala.util.Random

trait ItemGenerator {
  def generate(random: Random, depth: Int): Item
}
