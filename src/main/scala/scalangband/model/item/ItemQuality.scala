package scalangband.model.item

sealed trait ItemQuality

object NormalQuality extends ItemQuality
object GoodQuality extends ItemQuality
object GreatQuality extends ItemQuality
object Artifact extends ItemQuality