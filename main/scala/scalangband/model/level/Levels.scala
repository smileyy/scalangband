package scalangband.model.level

import scalangband.model.item.{Artifact, GoodQuality, GreatQuality, ItemQuality, NormalQuality}
import scalangband.model.util.{CenteredRange, Weighted}

object Levels {
  def apply(level: Int): LevelSpec = specs(level)
  
  private val specs: Map[Int, LevelSpec] = Map(
    1 -> LevelSpec(
      monsters = CenteredRange(1, 1 to 3), 
      items = CenteredRange(1, 1 to 5), 
      quality = Seq(
        Weighted(9900, NormalQuality), 
        Weighted(90, GoodQuality), 
        Weighted(9, GreatQuality), 
        Weighted(1, Artifact)
      )
    ),
    2 -> LevelSpec(
      monsters = CenteredRange(1, 1 to 3),
      items = CenteredRange(1, 1 to 5),
      quality = Seq(
        Weighted(9900, NormalQuality),
        Weighted(90, GoodQuality),
        Weighted(9, GreatQuality),
        Weighted(1, Artifact)
      )
    ),
  )
}

class LevelSpec(val monsters: CenteredRange, val items: CenteredRange, val quality: Seq[Weighted[ItemQuality]])