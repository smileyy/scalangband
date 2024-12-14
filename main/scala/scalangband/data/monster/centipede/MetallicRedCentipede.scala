package scalangband.data.monster.centipede

import scalangband.bridge.rendering.TextColors
import scalangband.model.element.{Cold, Fire}
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.{CrawlAttack, StingAttack}
import scalangband.model.monster.{Centipede, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object MetallicRedCentipede extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Metallic Red Centipede",
    archetype = Centipede,
    depth = 3,
    health = DiceRoll("4d6+3"),
    armorClass = 10,
    actions = actions,
    inventory = None,
    baseXp = 12,
    color = TextColors.Red
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(50, MeleeAttacksAction(Seq(CrawlAttack(DiceRoll("1d2")), StingAttack(DiceRoll("1d2"), element = Some(Fire))))),
      Weighted(50, RandomMovementAction)
    ),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
