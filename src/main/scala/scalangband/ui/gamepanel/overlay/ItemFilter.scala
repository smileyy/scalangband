package scalangband.ui.gamepanel.overlay

import scalangband.model.item.{EquippableItem, Item}

trait ItemFilter {
  def apply(item: Item): Boolean
  def accepts(item: Item): Boolean = apply(item)
}

object AllItemFilter extends ItemFilter {
  override def apply(item: Item): Boolean = true
}

object EquipmentFilter extends ItemFilter {
  override def apply(item: Item): Boolean = item.isInstanceOf[EquippableItem]
}
