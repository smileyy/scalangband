package scalangband.model.item

import scala.util.Random

trait ItemFactory {
  def apply(random: Random, quality: ItemQuality): Item

  def archetype: ItemArchetype
  def levels: Range
  def commonness: Int
}