package scalangband.model.item.lightsource

import scalangband.model.item.*

import scala.swing.Color
import scala.util.Random

trait LightSource extends EquippableItem {
  def radius: Int
}

class EphemeralLightSource(val spec: EphemeralLightSourceSpec, var turnsRemaining: Int) extends LightSource {
  def name: String = spec.name
  def archetype: ItemArchetype = spec.archetype

  def fillable: Boolean = spec.fillable
  def color: Color = spec.color

  override def displayName: String = s"$name ($turnsRemaining turns)"

  override def radius: Int = if (turnsRemaining > 0) spec.radius else 0

  override def onNextTurn(): Unit = {
    turnsRemaining = turnsRemaining - 1
  }
}

trait EphemeralLightSourceFactory extends ItemFactory {
  override def apply(random: Random = new Random, quality: ItemQuality = NormalQuality): LightSource = {
    new EphemeralLightSource(spec, spec.initialTurns)
  }

  def spec: EphemeralLightSourceSpec
  def archetype: ItemArchetype = spec.archetype
}

class EphemeralLightSourceSpec(
    val name: String,
    val radius: Int,
    val initialTurns: Int,
    val fillable: Boolean,
    val color: Color
) {
  val archetype: ItemArchetype = LightSource
}
