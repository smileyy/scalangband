package scalangband.data.monster.centipede

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{HearingBoundedAction, MeleeAttacks, MonsterActions, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.{CrawlAttack, StingAttack}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantYellowCentipede extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Giant Yellow Centipede",
    archetype = Centipede,
    depth = 1,
    health = DiceRoll("2d6"),
    hearing = 8,
    armorClass = 14,
    sleepiness = 20,
    experience = 2,
    actions = actions,
    color = TextColors.Yellow
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, MeleeAttacks(Seq(new CrawlAttack(DiceRoll("1d4")), new StingAttack(DiceRoll("1d4")))))
    ),
    los = Seq(Weighted(100, PathfindingAction)),
    otherwise = Seq(Weighted(100, new HearingBoundedAction(PathfindingAction, RandomMovementAction)))
  )
}
