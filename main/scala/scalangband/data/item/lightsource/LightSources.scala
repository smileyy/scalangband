package scalangband.data.item.lightsource

import scalangband.model.item.lightsource.EphemeralLightSourceFactory

object Torch extends EphemeralLightSourceFactory {
  override val name: String = "Torch"
  override val radius: Int = 1
  override val turns: Int = 5000
}