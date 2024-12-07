package scalangband.model.monster.mold

import scalangband.model.action.monster.MonsterPassAction
import scalangband.model.monster.{Mold, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object GreyMold extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Grey Mold",
    archetype = Mold,
    depth = 1,
    health = DiceRoll("1d3"),
    actions = Seq(Weighted(MonsterPassAction, 1)),
    color = TextColors.MediumGrey
  )
}
