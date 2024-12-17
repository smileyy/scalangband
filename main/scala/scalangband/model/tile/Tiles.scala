package scalangband.model.tile

import scalangband.model.{Creature, Representable}

abstract class Tile extends Representable {
  private var visible: Boolean = false
  var seen: Boolean = false

  def opaque: Boolean = false

  def isVisible: Boolean = visible

  def setVisible(visible: Boolean): Unit = {
    if (visible) {
      seen = true
    }
    this.visible = visible
  }

  def occupant: Option[Creature] = None
  def occupied: Boolean = occupant.isDefined

  def representation: Seq[Representable] = Seq(this)
}

abstract class OccupiableTile(var creature: Option[Creature]) extends Tile {
  override def occupant: Option[Creature] = creature

  def setOccupant(newOccupant: Creature): Unit = creature = Some(newOccupant)
  def removeOccupant(): Unit = creature = None
  
  override def representation: Seq[Representable] = Seq(occupant, Some(this)).flatten
}