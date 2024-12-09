package scalangband.model.monster.ant

import scalangband.model.monster.action.{MonsterAction, RandomMovementAction}
import scalangband.model.monster.{Ant, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object GiantWhiteAnt extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Giant White Ant",
    archetype = Ant,
    level = 2,
    health = DiceRoll("3d6"),
    armorClass = 19,
    actions = actions,
    inventory = None,
    color = TextColors.White
  )

  private def actions: Seq[Weighted[MonsterAction]] = {
    Seq(
      Weighted(100, RandomMovementAction)
    )
  }
}
