package scalangband.data.level.rooms.rectangle

import scalangband.model.level.generation.room.{RoomBuilder, RoomBuilderGenerator}

object TinyChamberRoom extends RoomBuilderGenerator {
  override def builder(top: Int, left: Int, height: Int, width: Int, depth: Int): RoomBuilder = {
    new RoomBuilder(top, left, height, width, depth)
      .row().w.w.w.w.w.w.w.w.w.build()
      .row().w.w.w.w.T.w.w.w.w.build()
      .row().w.w.f.f.f.f.f.w.w.build()
      .row().w.w.f.w.d.w.f.w.w.build()
      .row().w.L.f.w.m.w.f.R.w.build()
      .row().w.w.f.w.w.w.f.w.w.build()
      .row().w.w.f.f.f.f.f.w.w.build()
      .row().w.w.w.w.B.w.w.w.w.build()
      .row().w.w.w.w.w.w.w.w.w.build()
  }

  override def height: Int = 9
  override def width: Int = 9
}
