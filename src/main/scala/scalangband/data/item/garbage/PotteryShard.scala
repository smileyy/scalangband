package scalangband.data.item.garbage

import scalangband.bridge.rendering.TextColors.LightUmber
import scalangband.model.item.*

import scala.util.Random

object PotteryShard extends ItemFactory {
  override def apply(random: Random = new Random(), quality: ItemQuality): Item =
    new BasicItem("Pottery Shard", Miscellaneous, LightUmber)

  override val archetype: ItemArchetype = Miscellaneous
  override val levels: Range = 0 to 0
  override val commonness: Int = 0
}
