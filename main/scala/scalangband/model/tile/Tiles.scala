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

  def occupant: Option[Creature]
  def occupied: Boolean = occupant.isDefined

  def representation: Representable = if (occupant.isDefined) occupant.get else this
}

abstract class OccupiableTile(var occupant: Option[Creature]) extends Tile {
  def setOccupant(occupant: Creature): Unit = this.occupant = Some(occupant)
  def clearOccupant(): Unit = this.occupant = None
}