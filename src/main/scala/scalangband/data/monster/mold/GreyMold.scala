package scalangband.data.monster.mold

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, MonsterPassAction}
import scalangband.model.monster.attack.SporeAttack
import scalangband.model.monster.{Mold, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GreyMold extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Grey Mold",
    archetype = Mold,
    depth = 1,
    health = DiceRoll("1d3"),
    hearing = 2,
    armorClass = 1,
    sleepiness = 0,
    experience = 3,
    alive = false,
    actions = actions,
    color = TextColors.MediumGrey
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, MeleeAttacksAction(Seq(new SporeAttack(DiceRoll("1d4")), new SporeAttack(DiceRoll("1d4")))))
    ),
    otherwise = Seq(Weighted(100, MonsterPassAction))
  )
}
