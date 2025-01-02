package scalangband.data.monster.ant

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{Ant, MonsterFactory, MonsterFactoryFriendSpec, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantWhiteAnt extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Giant White Ant",
    archetype = Ant,
    depth = 3,
    health = DiceRoll("3d6"),
    hearing = 8,
    armorClass = 19,
    sleepiness = 80,
    experience = 2,
    actions = actions,
    friends = Seq(MonsterFactoryFriendSpec(100, DiceRoll("4d4"), GiantWhiteAnt)),
    color = TextColors.White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, MeleeAttacksAction(Seq(BiteAttack(DiceRoll("1d4")))))
    ),
    otherwise = Seq(Weighted(100, PathfindingAction))
  )
}
