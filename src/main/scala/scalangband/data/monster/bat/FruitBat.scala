package scalangband.data.monster.bat

import scalangband.bridge.rendering.TextColors.Orange
import scalangband.model.monster.action.{
  HearingBoundedAction,
  MeleeAttacksAction,
  MonsterActions,
  PathfindingAction,
  RandomMovementAction
}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{Bat, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object FruitBat extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Fruit Bat",
    archetype = Bat,
    depth = 1,
    speed = 30,
    health = DiceRoll("2d5"),
    hearing = 20,
    armorClass = 3,
    sleepiness = 10,
    experience = 1,
    actions = actions,
    color = Orange
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(75, MeleeAttacksAction(new BiteAttack(DiceRoll("1d1")))),
      Weighted(25, RandomMovementAction)
    ),
    los = Seq(Weighted(75, PathfindingAction), Weighted(25, RandomMovementAction)),
    otherwise = Seq(
      Weighted(75, HearingBoundedAction(PathfindingAction, RandomMovementAction)),
      Weighted(25, RandomMovementAction)
    )
  )
}
