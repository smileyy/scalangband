package scalangband.model.location

case class Coordinates(rowIdx: Int, colIdx: Int) {
  def +(direction: Direction): Coordinates = {
    Coordinates(rowIdx + direction.dy, colIdx + direction.dx)
  }

  override def toString: String = s"($rowIdx, $colIdx)"
}
