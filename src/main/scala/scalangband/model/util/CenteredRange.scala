package scalangband.model.util

import scala.util.Random

case class CenteredRange(center: Int, range: Range) {
  def randomInRange(random: Random): Int = {
    LazyList.continually(nextGaussian(random)).filter(x => x >= range.head && x <= range.last).head
  }

  private def inclusiveGap = range.last - range.head + 1

  private def nextGaussian(random: Random): Int = {
    (random.nextGaussian() * (inclusiveGap / 3) + center).round.toInt
  }
}