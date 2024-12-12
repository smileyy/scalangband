package scalangband.data.monster.ant

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{Ant, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantWhiteAnt extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Giant White Ant",
    archetype = Ant,
    depth = 3,
    health = DiceRoll("3d6"),
    armorClass = 19,
    actions = actions,
    inventory = None,
    color = TextColors.White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, MeleeAttacksAction(Seq(BiteAttack(DiceRoll("1d4")))))
    ), 
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
