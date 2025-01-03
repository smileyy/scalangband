package scalangband.model.item.lightsource

import scalangband.model.item.EquippableItem

trait LightSource extends EquippableItem {
  def radius: Int
}
