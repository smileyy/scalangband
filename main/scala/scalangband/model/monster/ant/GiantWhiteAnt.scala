package scalangband.model.monster.ant

import scalangband.model.action.monster.{MonsterAction, RandomMovementAction}
import scalangband.model.monster.{Ant, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object GiantWhiteAnt extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Giant White Ant",
    archetype = Ant,
    depth = 2,
    health = DiceRoll("3d6"),
    evasion = 19,
    actions = actions,
    inventory = None,
    color = TextColors.White
  )

  private def actions: Seq[Weighted[MonsterAction]] = {
    Seq(
      Weighted(RandomMovementAction, 100)
    )
  }
}
