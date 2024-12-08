package scalangband.data.monster.centipede

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{MonsterAction, RandomMovementAction}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object MetallicRedCentipede extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Metallic Red Centipede",
    archetype = Centipede,
    level = 3,
    health = DiceRoll("4d6+3"),
    armorClass = 10,
    actions = actions,
    inventory = None,
    color = TextColors.Red
  )
  
  private def actions = Seq[Weighted[MonsterAction]](
    Weighted(100, RandomMovementAction)
  )
}
