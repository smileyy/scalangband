package scalangband.data.monster.snake

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.monster.action.{
  HearingBoundedAction,
  MeleeAttacksAction,
  MonsterActions,
  PathfindingAction,
  RandomMovementAction
}
import scalangband.model.monster.attack.{BiteAttack, CrushAttack}
import scalangband.model.monster.{MonsterFactory, MonsterSpec, Snake}
import scalangband.model.util.{DiceRoll, Weighted}

object LargeWhiteSnake extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Large White Snake",
    archetype = Snake,
    depth = 1,
    speed = 10,
    health = DiceRoll("3d6"),
    hearing = 4,
    armorClass = 36,
    sleepiness = 99,
    experience = 2,
    actions = actions,
    color = White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(50, MeleeAttacksAction(Seq(new BiteAttack(DiceRoll("1d2")), new CrushAttack(DiceRoll("1d2"))))),
      Weighted(50, RandomMovementAction)
    ),
    los = Seq(Weighted(50, PathfindingAction), Weighted(50, RandomMovementAction)),
    otherwise = Seq(
      Weighted(50, HearingBoundedAction(PathfindingAction, RandomMovementAction)),
      Weighted(50, RandomMovementAction)
    )
  )
}
