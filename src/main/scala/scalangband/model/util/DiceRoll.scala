package scalangband.model.util

import scala.util.Random

case class DiceRoll(numberOfDice: Int, numberOfSides: Int, constant: Int = 0) {
  def roll(): Int = (0 until numberOfDice).map(_ => Random.nextInt(numberOfSides) + 1).sum + constant
  def max: Int = (0 until numberOfDice).map(_ => numberOfSides).sum + constant
  
  override def toString: String = s"${numberOfDice}d$numberOfSides${ if (constant == 0) "" else "+" + constant }"
}
object DiceRoll {
  def apply(rollSpec: String): DiceRoll = {
    def splitDice(diceSpec: String): (Int, Int) = {
      val Array(number, sides) = diceSpec.split('d')
      (number.toInt, sides.toInt)
    }

    val ((numberOfDice, numberOfSides), constant) = rollSpec.split('+') match {
      case Array(dice) => (splitDice(dice), 0)
      case Array(dice, constant) => (splitDice(dice), constant.toInt)
    }

    new DiceRoll(numberOfDice, numberOfSides, constant)
  }
}