package scalangband.data.monster.centipede

import scalangband.bridge.rendering.TextColors
import scalangband.model.element.Cold
import scalangband.model.monster.action.{
  HearingBoundedAction,
  MeleeAttacksAction,
  MonsterActions,
  PathfindingAction,
  RandomMovementAction
}
import scalangband.model.monster.attack.{CrawlAttack, StingAttack}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantWhiteCentipede extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Giant White Centipede",
    archetype = Centipede,
    depth = 1,
    health = DiceRoll("3d5"),
    hearing = 7,
    armorClass = 11,
    sleepiness = 40,
    experience = 2,
    actions = actions,
    color = TextColors.White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(
        50,
        MeleeAttacksAction(Seq(CrawlAttack(DiceRoll("1d2")), StingAttack(DiceRoll("1d2"), element = Some(Cold))))
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
