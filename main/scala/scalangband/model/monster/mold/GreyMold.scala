package scalangband.model.monster.mold

import scalangband.model.monster.action.MonsterPassAction
import scalangband.model.monster.{Mold, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object GreyMold extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Grey Mold",
    archetype = Mold,
    level = 1,
    health = DiceRoll("1d3"),
    armorClass = 1,
    actions = Seq(Weighted(1, MonsterPassAction)),
    color = TextColors.MediumGrey
  )
}
