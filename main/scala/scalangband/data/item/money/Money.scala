package scalangband.data.item.money

import scalangband.model.item.{Item, ItemArchetype, MoneyArchetype}

import scala.swing.Color

class Money(val name: String, val amount: Int, val color: Color, val material: String) extends Item {
  override def archetype: ItemArchetype = MoneyArchetype

  override def displayName: String = s"Pile of $material"
}