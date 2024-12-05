package scalangband.model.item

trait ItemGenerator {
  def generate(level: Int): Item
}
