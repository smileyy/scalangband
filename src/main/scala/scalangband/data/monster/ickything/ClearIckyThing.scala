package scalangband.data.monster.ickything

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.TouchAttack
import scalangband.model.monster.{IckyThing, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object ClearIckyThing extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Clear Icky Thing",
    archetype = IckyThing,
    depth = 1,
    health = DiceRoll("2d5"),
    hearing = 12,
    armorClass = 7,
    sleepiness = 10,
    experience = 2,
    invisible = true,
    clear = true,
    actions = actions,
    color = White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(25, RandomMovementAction),
      Weighted(75, MeleeAttacksAction(new TouchAttack(DiceRoll("1d2"))))
    ),
    otherwise = Seq(
      Weighted(25, RandomMovementAction),
      Weighted(75, RandomMovementAction)
    )
  )
}
