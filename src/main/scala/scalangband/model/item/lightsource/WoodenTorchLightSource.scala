package scalangband.model.item.lightsource

import scalangband.model.item.*

import scala.swing.Color
import scala.util.Random

class WoodenTorchLightSource(val spec: WoodenTorchSpec, var turnsRemaining: Int, var count: Int) extends LightSource, StackableItem {
  def name: String = spec.name
  def archetype: ItemArchetype = spec.archetype

  def color: Color = spec.color

  override def quantity: Int = count
  override def increment(quantity: Int): Unit = count += quantity
  override def decrement(quantity: Int): Unit = count -= quantity

  override def clone(quantity: Int): StackableItem = new WoodenTorchLightSource(spec, turnsRemaining, quantity)

  override def stacksWith(item: Item): Boolean = item match {
    case torch: WoodenTorchLightSource => torch.turnsRemaining == turnsRemaining
    case _ => false
  }

  override def radius: Int = if (turnsRemaining > 0) spec.radius else 0

  override def onNextTurn(): Unit = {
    turnsRemaining = turnsRemaining - 1
  }

  override def singular: String = s"${spec.singular} ($turnsRemaining turns)"
  override def plural: String = s"${spec.plural} ($turnsRemaining turns)"
}

trait WoodenTorchFactory extends StackableItemFactory {
  override def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality, quantity: Option[Int]): WoodenTorchLightSource = {
    new WoodenTorchLightSource(spec, spec.initialTurns, quantity.getOrElse(1))
  }

  def spec: WoodenTorchSpec
  def archetype: ItemArchetype = spec.archetype
}

class WoodenTorchSpec(
    val radius: Int,
    val initialTurns: Int,
    val color: Color
) {
  val name: String = "Wooden Torch"
  val archetype: ItemArchetype = LightSource
  val singular: String = "a Wooden Torch"
  val plural: String = "Wooden Torches"
}
