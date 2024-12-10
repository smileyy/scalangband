package scalangband.data.item.money

import scalangband.model.item.Item

trait Money extends Item {
  def amount: Int
  def material: String
}

case class CopperCoins(amount: Int) extends Money {
  override def name: String = "Pile of copper coins"
  override def displayName: String = "pile of copper coins"
  override def material: String = "copper"
}