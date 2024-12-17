package scalangband.data.monster.centipede

import scalangband.bridge.rendering.TextColors
import scalangband.model.element.Fire
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.{CrawlAttack, StingAttack}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object MetallicRedCentipede extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Metallic Red Centipede",
    archetype = Centipede,
    depth = 3,
    health = DiceRoll("4d6+3"),
    armorClass = 10,
    experience = 12,
    sleepiness = 20,
    actions = actions,
    color = TextColors.Red
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(
        50,
        MeleeAttacksAction(Seq(CrawlAttack(DiceRoll("1d2")), StingAttack(DiceRoll("1d2"), element = Some(Fire))))
      ),
      Weighted(50, RandomMovementAction)
    ),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
