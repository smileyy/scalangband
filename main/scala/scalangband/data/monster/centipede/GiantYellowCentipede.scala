package scalangband.data.monster.centipede

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.{CrawlAttack, MeleeAttack, StingAttack}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantYellowCentipede extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Giant Yellow Centipede",
    archetype = Centipede,
    depth = 1,
    health = DiceRoll("2d6"),
    armorClass = 14,
    experience = 2,
    sleepiness = 20,
    actions = actions,
    color = TextColors.Yellow
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, MeleeAttacksAction(Seq(new CrawlAttack(DiceRoll("1d4")), new StingAttack(DiceRoll("1d4")))))
    ),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
