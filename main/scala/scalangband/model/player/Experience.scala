package scalangband.model.player

case class Experience(value: Int, max: Int) {
  def +(xp: Int): Experience = {
    Experience(value + xp, max + xp)
  }
}
object Experience {
  def None: Experience = Experience(0, 0)
}
