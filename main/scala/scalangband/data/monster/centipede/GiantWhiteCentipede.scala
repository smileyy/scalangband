package scalangband.data.monster.centipede

import scalangband.bridge.rendering.TextColors
import scalangband.model.element.Cold
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.{CrawlAttack, StingAttack}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantWhiteCentipede extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Giant White Centipede",
    archetype = Centipede,
    depth = 1,
    health = DiceRoll("3d5"),
    armorClass = 11,
    experience = 2,
    sleepiness = 40,
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
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
