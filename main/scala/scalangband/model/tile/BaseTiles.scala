package scalangband.model.tile

import scalangband.model.{Creature, Representable}
import scalangband.model.location.Coordinates

abstract class Tile(val coordinates: Coordinates, var seen: Boolean = false, private var _visible: Boolean = false) extends Representable {
  def opaque: Boolean = false

  def representation: Representable = this

  def visible: Boolean = _visible

  def setVisible(visible: Boolean): Unit = {
    if (visible) {
      seen = true
    }

    _visible = visible
  }

  override def toString: String = s"${this.getClass}$coordinates"
}

abstract class OccupiableTile(coordinates: Coordinates) extends Tile(coordinates) {
  // This is a `def` because I had a lot of trouble getting `Floor` to work with occupant as an abstract class member :(
  // It isn't too horrible because there's only a handful of occupiable tile types
  def occupant: Option[Creature]

  def setOccupant(occupant: Creature): Unit
  def clearOccupant(): Unit

  def occupied: Boolean = occupant.isDefined

  override def representation: Representable = if (occupant.isDefined) occupant.get else this
}