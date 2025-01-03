package scalangband.model.item.food

import scalangband.model.player.Player.MaxSatiety
import scalangband.model.util.{DiceRoll, Weighted}

import scala.swing.Color
import scala.util.Random

class FoodSpec(
    val name: String,
    val singular: String,
    val plural: String,
    val quantities: Seq[Weighted[DiceRoll]],
    val satiety: Satiety,
    val message: String = "That tastes good.",
    val color: Color
) {
  def quantity(random: Random): Int = Weighted.selectFrom(random, quantities).roll()
}

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
