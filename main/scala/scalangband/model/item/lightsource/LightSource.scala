package scalangband.model.item.lightsource

import scalangband.model.item.EquippableItem

trait LightSource extends EquippableItem {
  def radius: Int
}

class EphemeralLightSource(override val name: String, baseRadius: Int, var turnsRemaining: Int)
  extends LightSource {
  def displayName: String = s"$name ($turnsRemaining turns)"

  override def radius: Int = if (turnsRemaining > 0) baseRadius else 0

  override def onNextTurn(): Unit = {
    turnsRemaining = turnsRemaining - 1
  }
}

trait EphemeralLightSourceFactory {
  def apply(): LightSource = {
    new EphemeralLightSource(name, radius, turns)
  }

  def name: String
  def radius: Int
  def turns: Int
}

class PermanentLightSource(val name: String, override val radius: Int, everburning: Boolean) extends LightSource {
  override def displayName: String = {
    def suffix: String = if (everburning) " (Everburning)" else ""
    s"$name$suffix"
  }
}

trait PermanentLightSourceFactory {
  def apply(): LightSource = {
    new PermanentLightSource(name, radius, everburning)
  }

  def name: String
  def radius: Int
  def everburning: Boolean = true
}