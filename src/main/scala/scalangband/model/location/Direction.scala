package scalangband.model.location

import scala.util.Random

sealed abstract class Direction(val dx: Int, val dy: Int) {
  def opposite: Direction
}
object Direction {
  val allDirections: Seq[Direction] = IndexedSeq(
    UpDirection, 
    UpRightDirection, 
    RightDirection, 
    DownRightDirection,
    DownDirection, 
    DownLeftDirection, 
    LeftDirection, 
    UpLeftDirection
  )
  
  val cardinalDirections: Seq[Direction] = IndexedSeq(
    UpDirection, DownDirection, LeftDirection, RightDirection
  )
  
  def randomCardinalDirection(random: Random = new Random()): Direction = {
    cardinalDirections(random.nextInt(cardinalDirections.size))
  }
  
  def randomDirection(): Direction = allDirections(Random.nextInt(8))
}

case object UpDirection extends Direction(0, -1) {
  override def opposite: Direction = DownDirection
}
case object DownDirection extends Direction(0, 1):
  override def opposite: Direction = UpDirection

case object LeftDirection extends Direction(-1, 0) {
  override def opposite: Direction = RightDirection
}
case object RightDirection extends Direction(1, 0) {
  override def opposite: Direction = LeftDirection
}

case object UpLeftDirection extends Direction(-1, -1) {
  override def opposite: Direction = DownRightDirection
}
case object UpRightDirection extends Direction(1, -1) {
  override def opposite: Direction = DownLeftDirection
}
case object DownLeftDirection extends Direction(-1, 1) {
  override def opposite: Direction = UpRightDirection
}
case object DownRightDirection extends Direction(1, 1) {
  override def opposite: Direction = UpLeftDirection
}