package scalangband.model.location

case class Coordinates(row: Int, col: Int) {
  def x: Int = col
  def y: Int = row
  
  def +(direction: Direction): Coordinates = {
    Coordinates(row + direction.dy, col + direction.dx)
  }

  def euclidianDistanceTo(target: Coordinates): Int = {
    val dy = y - target.y
    val dx = x - target.x
    
    math.sqrt(dy*dy + dx*dx).toInt
  }

  def chebyshevDistance(target: Coordinates): Int = {
    math.max(math.abs(y - target.y), math.abs(x - target.x))
  }
  
  override def toString: String = s"($row, $col)"
}
object Coordinates {
  val Placeholder: Coordinates = Coordinates(-1, -1)
}
