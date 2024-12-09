package scalangband.data.monster.mold

import scalangband.bridge.rendering.TextColors
import scalangband.model.monster.action.{AdjacentToPlayerActions, MeleeAttacksAction, MonsterAction, MonsterPassAction}
import scalangband.model.monster.attack.SporeAttack
import scalangband.model.monster.{Mold, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object GreyMold extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Grey Mold",
    archetype = Mold,
    level = 1,
    health = DiceRoll("1d3"),
    armorClass = 1,
    actions = actions,
    color = TextColors.MediumGrey
  )

  private def actions: Seq[Weighted[MonsterAction]] = Seq(
    Weighted(100, AdjacentToPlayerActions(
      adjacent = Seq(Weighted(100, MeleeAttacksAction(Seq(
        new SporeAttack(DiceRoll("1d4")),
        new SporeAttack(DiceRoll("1d4"))
      )))),
      otherwise = Seq(Weighted(100, MonsterPassAction))
    ))
  )

}
