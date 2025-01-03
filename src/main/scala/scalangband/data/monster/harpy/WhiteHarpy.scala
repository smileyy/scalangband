package scalangband.data.monster.harpy

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.monster.action.*
import scalangband.model.monster.attack.{BiteAttack, ClawAttack}
import scalangband.model.monster.{Harpy, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object WhiteHarpy extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "White Harpy",
    archetype = Harpy,
    depth = 2,
    health = DiceRoll("2d4+1"),
    hearing = 16,
    armorClass = 20,
    sleepiness = 10,
    experience = 5,
    actions = actions,
    color = White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(
        50,
        MeleeAttacks(
          Seq(new ClawAttack(DiceRoll("1d1")), new ClawAttack(DiceRoll("1d1")), new BiteAttack(DiceRoll("1d2")))
        )
      ),
      Weighted(50, RandomMovementAction)
    ),
    los = Seq(Weighted(50, PathfindingAction), Weighted(50, RandomMovementAction)),
    otherwise = Seq(
      Weighted(50, HearingBoundedAction(PathfindingAction, RandomMovementAction)),
      Weighted(50, RandomMovementAction)
    )
  )
}
