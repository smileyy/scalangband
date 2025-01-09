package scalangband.data.level.rooms.rectangle

import scalangband.model.level.generation.room.{Room, RoomBuilder, RoomGenerator}

import scala.util.Random

object TinyChamberRoom extends RoomGenerator {
  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = {
    RoomBuilder{
      """wwwwwwww#
        |wwwwTwwww
        |ww     ww
        |ww wdw ww
        |wL wmw Rw
        |ww www ww
        |ww     ww
        |wwwwBwwww
        |wwwwwwwww
        |""".stripMargin.trim
    }(top, left, depth)
  }
}
