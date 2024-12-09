package scalangband.data.item.money

import scalangband.model.item.Item

trait Money extends Item {
  def amount: Int
}

case class CopperCoins(amount: Int) extends Money {
  override def name: String = "Pile of copper coins"
  override def displayName: String = "a pile of copper coins"
}