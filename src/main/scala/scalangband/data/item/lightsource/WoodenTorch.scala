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