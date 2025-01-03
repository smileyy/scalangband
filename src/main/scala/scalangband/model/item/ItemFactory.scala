package scalangband.model.item

import scala.util.Random

trait ItemFactory {
  def apply(random: Random, quality: ItemQuality): Item

  def archetype: ItemArchetype
  def levels: Range
  def commonness: Int
}

trait StackableItemFactory extends ItemFactory {
  override def apply(random: Random, quality: ItemQuality): Item = {
    apply(random, quality, None)
  }
  
  def apply(random: Random, quality: ItemQuality, quantity: Option[Int]): Item
}