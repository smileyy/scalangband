package scalangband.model.location

sealed abstract class Direction(val dx: Int, val dy: Int)
case object Up extends Direction(0, -1)
case object Down extends Direction(0, 1)
case object Left extends Direction(-1, 0)
case object Right extends Direction(1, 0)

case object UpLeft extends Direction(-1, -1)
case object UpRight extends Direction(1, -1)
case object DownLeft extends Direction(-1, 1)
case object DownRight extends Direction(1, 1)
