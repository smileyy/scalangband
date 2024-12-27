package scalangband.data.monster.bird

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{Bird, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object Crow extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Crow",
    archetype = Bird,
    depth = 2,
    health = DiceRoll("3d5"),
    armorClass = 14,
    experience = 8,
    sleepiness = 0,
    actions = actions,
    color = TextColors.DarkGrey
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, MeleeAttacksAction(Seq(BiteAttack(DiceRoll("1d3")), BiteAttack(DiceRoll("1d3")))))
    ),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
