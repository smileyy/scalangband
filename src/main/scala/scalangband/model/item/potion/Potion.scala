package scalangband.model.item.potion

import scalangband.bridge.actionresult.ActionResult
import scalangband.bridge.rendering.TextColors.White
import scalangband.model.item.{Item, ItemArchetype, ItemFactory, ItemQuality, Potion}
import scalangband.model.player.PlayerCallback

import scala.swing.Color

class Potion(spec: PotionSpec) extends Item {
  override def name: String = spec.name
  override def archetype: ItemArchetype = Potion
  override def color: Color = spec.color
  def satiety: Int = spec.satiety
  
  override def singular: String = s"Potion of $name"
  
  def onQuaff(callback: PlayerCallback): List[ActionResult] = spec.onQuaff(callback)
}

trait PotionFactory extends ItemFactory {
  override def archetype: ItemArchetype = Potion
  def spec: PotionSpec
}

class PotionSpec(
  val name: String,
  val effect: PotionEffect,
  val satiety: Int = 1,
  val color: Color = White
) {
  def onQuaff(callback: PlayerCallback): List[ActionResult] = effect.onQuaff(callback)
}

trait PotionEffect {
  def onQuaff(callback: PlayerCallback): List[ActionResult]
}