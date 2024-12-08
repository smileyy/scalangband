package scalangband.model.monster.centipede

import scalangband.model.action.monster.RandomMovementAction
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object GiantYellowCentipede extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Giant Yellow Centipede",
    archetype = Centipede,
    depth = 1,
    health = DiceRoll("2d6"),
    evasion = 14,
    actions = Seq(Weighted(RandomMovementAction, 100)),
    color = TextColors.Yellow
  )
}
