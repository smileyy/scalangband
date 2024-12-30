package scalangband.model.util

import scalangband.model.location.Coordinates

/** Heavily adapted from https://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Scala
  */
class BresenhamLine(from: Coordinates, to: Coordinates) extends Iterator[Coordinates] {
  private val dx = math.abs(to.x - from.x)
  private val sx = if (from.x < to.x) 1 else -1
  private val dy = math.abs(to.y - from.y)
  private val sy = if (from.y < to.y) 1 else -1

  private var x = from.x
  private var y = from.y

  override def hasNext: Boolean = sx * x <= sx * to.x && sy * y <= sy * to.y

  override def next(): Coordinates = {
    var err = (if (dx > dy) dx else -dy) / 2

    // `Coordinates` is (row, col), so "invert" x and y
    val res = Coordinates(y, x)
    val e2 = err
    if (e2 > -dx) {
      err -= dy
      x += sx
    }
    if (e2 < dy) {
      err += dx
      y += sy
    }
    res
  }
}
