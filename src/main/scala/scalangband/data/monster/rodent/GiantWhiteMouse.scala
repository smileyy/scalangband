package scalangband.data.monster.rodent

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.monster.action.{HearingBoundedAction, MeleeAttacks, MonsterActions, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{MonsterFactory, MonsterSpec, Rodent}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantWhiteMouse extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Giant White Mouse",
    archetype = Rodent,
    depth = 1,
    health = DiceRoll("1d3"),
    hearing = 8,
    armorClass = 4,
    sleepiness = 20,
    experience = 1,
    breeds = true,
    actions = actions,
    color = White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(50, MeleeAttacks(new BiteAttack(DiceRoll("1d2")))),
      Weighted(50, RandomMovementAction)
    ),
    los = Seq(
      Weighted(50, PathfindingAction),
      Weighted(50, RandomMovementAction)
    ),
    otherwise = Seq(
      Weighted(50, HearingBoundedAction(PathfindingAction, RandomMovementAction)),
      Weighted(50, RandomMovementAction)
    )
  )

}
