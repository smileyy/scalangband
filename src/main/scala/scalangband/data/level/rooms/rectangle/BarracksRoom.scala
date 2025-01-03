package scalangband.data.level.rooms.rectangle

import scalangband.model.level.generation.room.{Room, RoomBuilder, RoomGenerator}

import scala.util.Random

object BarracksRoom extends RoomGenerator {
  private val designs = Vector(
    """wwwwwwwwwwwwwwwwwwwww
      |wwwwTwwwwwTwwwwwTwwww
      |wL                 Rw
      |wL                 Rw
      |wwwwdwwwwwdwwwwwdwwww
      |ww     w m   w   m ww
      |ww  m  w     w     ww
      |wwwwBwwwwwBwwwwwBwwww
      |wwwwwwwwwwwwwwwwwwwww
      |""".stripMargin.trim,

    """wwwwwwwwwwwwwwwwwwwww
      |wwwwTwwwwwTwwwwwTwwww
      |ww     w     w  m  ww
      |ww m   w     w     ww
      |wwwwdwwwww wwwwwdwwww
      |wL        m        Rw
      |wL                 Rw
      |wwwwBwwwwwBwwwwwBwwww
      |wwwwwwwwwwwwwwwwwwwww
      |""".stripMargin.trim,

    """wwwwwwwww
      |wwTTwwwww
      |ww  w  ww
      |ww  w  ww
      |wL  d mRw
      |ww  w  ww
      |ww  w  ww
      |ww  wwwww
      |ww  w  ww
      |ww  wm ww
      |wL  d  Rw
      |ww  w  ww
      |ww  w  ww
      |wwBBwwwww
      |wwwwwwwww
      |""".stripMargin.trim
  )

  override def generateRoom(random: Random, top: Int, left: Int, depth: Int): Room = {
    RoomBuilder{
      designs(random.nextInt(designs.size))
    }(top, left, depth)
  }
}
