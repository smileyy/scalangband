package scalangband.model.location

import scala.util.Random

sealed abstract class Direction(val dx: Int, val dy: Int) {
  def opposite: Direction
}
object Direction {
  val allDirections: Seq[Direction] = IndexedSeq(Up, UpRight, Right, DownRight, Down, DownLeft, Left, UpLeft)
  def randomDirection(): Direction = allDirections(Random.nextInt(8))
}

case object Up extends Direction(0, -1) {
  override def opposite: Direction = Down
}
case object Down extends Direction(0, 1):
  override def opposite: Direction = Up

case object Left extends Direction(-1, 0) {
  override def opposite: Direction = Right
}
case object Right extends Direction(1, 0) {
  override def opposite: Direction = Left
}

case object UpLeft extends Direction(-1, -1) {
  override def opposite: Direction = DownRight
}
case object UpRight extends Direction(1, -1) {
  override def opposite: Direction = DownLeft
}
case object DownLeft extends Direction(-1, 1) {
  override def opposite: Direction = UpRight
}
case object DownRight extends Direction(1, 1) {
  override def opposite: Direction = UpLeft
}
