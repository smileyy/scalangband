package scalangband.model.item.food

import scalangband.model.player.Player.MaxSatiety

import scala.swing.Color

class FoodSpec(val name: String, val satiety: Satiety, val message: String = "That tastes good.", val color: Color)

sealed trait Satiety {
  def whenEaten(current: Int): Int
}

case class IncreaseSatietyBy(increaseBy: Int) extends Satiety {
  override def whenEaten(current: Int): Int = {
    if (current + increaseBy > MaxSatiety) MaxSatiety else current + increaseBy
  }
}

case class IncreateSatietyTo(increaseTo: Int) extends Satiety {
  override def whenEaten(current: Int): Int = {
    if (current < increaseTo) increaseTo else current
  }
}
