package scalangband.data.item.lightsource

import scalangband.bridge.rendering.TextColors.LightUmber
import scalangband.model.item.lightsource.{EphemeralLightSourceFactory, EphemeralLightSourceSpec}

object Lantern extends EphemeralLightSourceFactory {
  override val spec: EphemeralLightSourceSpec = EphemeralLightSourceSpec(
    name = "Lantern", radius = 3, initialTurns = 7500, fillable = true, color = LightUmber
  )

  override def levels: Range = 5 to 100
  override def commonness: Int = 70
}