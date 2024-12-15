package scalangband.data.monster.ickything

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.TouchAttack
import scalangband.model.monster.{IckyThing, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object WhiteIckyThing extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "White Icky Thing",
    archetype = IckyThing,
    depth = 1,
    health = DiceRoll("2d5"),
    armorClass = 8,
    experience = 1,
    sleepiness = 10,
    actions = actions,
    color = White
  )

  private def actions = new MonsterActions(
    adjacent = Seq(
      Weighted(75, MeleeAttacksAction(Seq(new TouchAttack(DiceRoll("1d2"))))),
      Weighted(25, RandomMovementAction)
    ),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
