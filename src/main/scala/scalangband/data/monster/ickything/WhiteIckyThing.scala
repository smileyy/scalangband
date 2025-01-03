package scalangband.data.monster.ickything

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.monster.action.{
  HearingBoundedAction,
  MeleeAttacks,
  MonsterActions,
  PathfindingAction,
  RandomMovementAction
}
import scalangband.model.monster.attack.TouchAttack
import scalangband.model.monster.{IckyThing, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object WhiteIckyThing extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "White Icky Thing",
    archetype = IckyThing,
    depth = 1,
    health = DiceRoll("2d5"),
    hearing = 12,
    armorClass = 8,
    sleepiness = 10,
    experience = 1,
    actions = actions,
    color = White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(75, MeleeAttacks(Seq(new TouchAttack(DiceRoll("1d2"))))),
      Weighted(25, RandomMovementAction)
    ),
    los = Seq(Weighted(75, PathfindingAction), Weighted(25, RandomMovementAction)),
    otherwise = Seq(
      Weighted(75, HearingBoundedAction(PathfindingAction, RandomMovementAction)),
      Weighted(25, RandomMovementAction)
    )
  )
}
