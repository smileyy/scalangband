package scalangband.data.item.lightsource

import scalangband.bridge.rendering.TextColors.{LightUmber, Umber}
import scalangband.model.item.lightsource.{EphemeralLightSourceFactory, EphemeralLightSourceSpec}

object WoodenTorch extends EphemeralLightSourceFactory {
  override val spec: EphemeralLightSourceSpec = EphemeralLightSourceSpec(
    name = "Wooden Torch", radius = 2, initialTurns = 5000, fillable = false, color = Umber
  )

  override val levels: Range = 1 to 100
  override val commonness: Int = 70
}

object Lantern extends EphemeralLightSourceFactory {
  override val spec: EphemeralLightSourceSpec = EphemeralLightSourceSpec(
    name = "Lantern", radius = 3, initialTurns = 7500, fillable = true, color = LightUmber
  )

  override def levels: Range = 5 to 100
  override def commonness: Int = 70
}