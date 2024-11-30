package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.location.Coordinates

abstract class Tile(val coordinates: Coordinates) extends Representable {
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

  override def toString: String = s"${this.getClass.getSimpleName}$coordinates"
}

abstract class OccupiableTile(coordinates: Coordinates, var occupant: Option[Creature]) extends Tile(coordinates) {
  def setOccupant(occupant: Creature): Unit = this.occupant = Some(occupant)
  def clearOccupant(): Unit = this.occupant = None
}