package scalangband.model.util

import scalangband.model.location.Coordinates

/** Heavily adapted from https://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Scala
  */
class BresenhamLine(start: Coordinates, end: Coordinates) extends Iterator[Coordinates] {
  private val dx = math.abs(end.x - start.x)
  private val sx = if (start.x < end.x) 1 else -1
  private val dy = math.abs(end.y - start.y)
  private val sy = if (start.y < end.y) 1 else -1

  private var x = start.x
  private var y = start.y

  override def hasNext: Boolean = sx * x <= sx * end.x && sy * y <= sy * end.y

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
