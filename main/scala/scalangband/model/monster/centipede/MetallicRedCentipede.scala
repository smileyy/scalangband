package scalangband.model.monster.centipede

import scalangband.model.action.monster.{MonsterAction, RandomMovementAction}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object MetallicRedCentipede extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Metallic Red Centipede",
    archetype = Centipede,
    depth = 3,
    health = DiceRoll("4d6+3"),
    actions = actions,
    inventory = None,
    color = TextColors.Red
  )
  
  private def actions = Seq[Weighted[MonsterAction]](
    Weighted(RandomMovementAction, 100)
  )
}
