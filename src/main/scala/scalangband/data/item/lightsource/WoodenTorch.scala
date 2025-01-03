package scalangband.data.item.lightsource

import scalangband.bridge.rendering.TextColors.Umber
import scalangband.model.item.lightsource.{WoodenTorchFactory, WoodenTorchSpec}

object WoodenTorch extends WoodenTorchFactory {
  override val spec: WoodenTorchSpec = WoodenTorchSpec(
    radius = 2, initialTurns = 5000, color = Umber
  )

  override val levels: Range = 1 to 100
  override val commonness: Int = 70
}