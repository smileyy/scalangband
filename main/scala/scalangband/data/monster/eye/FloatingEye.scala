package scalangband.data.monster.eye

import scalangband.bridge.rendering.TextColors.Orange
import scalangband.model.effect.Paralysis
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, MonsterPassAction}
import scalangband.model.monster.attack.GazeAttack
import scalangband.model.monster.{Eye, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object FloatingEye extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Floating Eye",
    archetype = Eye,
    depth = 1,
    health = DiceRoll("4d4+1"),
    armorClass = 7,
    experience = 1,
    sleepiness = 10,
    actions = actions,
    color = Orange
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(20, MeleeAttacksAction(new GazeAttack(DiceRoll("1d1"), effect = Some(Paralysis(DiceRoll("1d4")))))),
      Weighted(80, MonsterPassAction),
    ),
    otherwise = Seq(Weighted(100, MonsterPassAction))
  )
}
