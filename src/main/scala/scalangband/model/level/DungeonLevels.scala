package scalangband.model.level

import scalangband.model.item.*
import scalangband.model.util.{CenteredRange, Weighted}

object DungeonLevels {
  def apply(level: Int): LevelSpec = specs(level)
  
  private val specs: Map[Int, LevelSpec] = Map(
    1 -> LevelSpec(
      monsters = CenteredRange(1, 1 to 1), 
      items = CenteredRange(1, 1 to 1),
      quality = Seq(
        Weighted(9900, NormalQuality), 
        Weighted(100, GoodQuality), 
      )
    ),
    2 -> LevelSpec(
      monsters = CenteredRange(2, 1 to 3),
      items = CenteredRange(2, 1 to 5),
      quality = Seq(
        Weighted(9900, NormalQuality),
        Weighted(100, GoodQuality),
      )
    ),
  )
}

class LevelSpec(val monsters: CenteredRange, val items: CenteredRange, val quality: Seq[Weighted[ItemQuality]])