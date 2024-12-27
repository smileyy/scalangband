package scalangband.model

case class Health(current: Int, max: Int) {
  def -(amount: Int): Health = Health(current - amount, max)
  def <=(amount: Int): Boolean = current <= amount

  def percent: Int = (current * 100) / max

  override def toString: String = s"$current/$max"
}
object Health {
  def fullHealth(health: Int): Health = new Health(health, health)
}