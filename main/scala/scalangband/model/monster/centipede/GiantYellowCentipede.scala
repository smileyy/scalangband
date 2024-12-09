package scalangband.model.monster.centipede

import scalangband.model.monster.action.{AdjacentToPlayerActions, MeleeAttacksAction, MonsterAction, RandomMovementAction}
import scalangband.model.monster.attack.{CrawlAttack, MeleeAttack, StingAttack}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object GiantYellowCentipede extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Giant Yellow Centipede",
    archetype = Centipede,
    level = 1,
    health = DiceRoll("2d6"),
    armorClass = 14,
    actions = actions,
    color = TextColors.Yellow
  )

  private def actions: Seq[Weighted[MonsterAction]] = Seq(
    Weighted(100, AdjacentToPlayerActions(
      adjacent = Seq(Weighted(100, MeleeAttacksAction(Seq(
        new CrawlAttack(DiceRoll("1d4")),
        new StingAttack(DiceRoll("1d4"))
      )))),
      otherwise = Seq(Weighted(100, RandomMovementAction))
    ))
  )
}
