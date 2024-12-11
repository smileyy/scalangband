package scalangband.model.location

case class Coordinates(row: Int, col: Int) {
  def +(direction: Direction): Coordinates = {
    Coordinates(row + direction.dy, col + direction.dx)
  }

  override def toString: String = s"($row, $col)"
}
object Coordinates {
  val Placeholder: Coordinates = Coordinates(-1, -1)
}
