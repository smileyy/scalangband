package scalangband.data.monster.worm

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.effect.Poisoning
import scalangband.model.element.Poison
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.CrawlAttack
import scalangband.model.monster.{MonsterFactory, MonsterSpec, Worm}
import scalangband.model.util.{DiceRoll, Weighted}

object WhiteWormMass extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "White Worm Mass",
    archetype = Worm,
    depth = 1,
    health = DiceRoll("4d4"),
    armorClass = 1,
    experience = 2,
    sleepiness = 14,
    breeds = true,
    actions = actions,
    color = White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(
        25,
        MeleeAttacksAction(
          new CrawlAttack(DiceRoll("1d2"), element = Some(Poison), effect = Some(Poisoning(DiceRoll("1d4"))))
        )),
      Weighted(75, RandomMovementAction)
    ),
    otherwise = Seq(
      Weighted(75, RandomMovementAction),
      Weighted(25, RandomMovementAction)
    )
  )
}
