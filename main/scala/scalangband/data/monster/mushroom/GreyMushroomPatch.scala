package scalangband.data.monster.mushroom

import scalangband.bridge.rendering.TextColors
import scalangband.model.effect.Confusion
import scalangband.model.monster.action.{MonsterActions, MeleeAttacksAction, MonsterAction, MonsterPassAction}
import scalangband.model.monster.attack.SporeAttack
import scalangband.model.monster.{MonsterFactory, MonsterSpec, Mushroom}
import scalangband.model.util.{DiceRoll, Weighted}

object GreyMushroomPatch extends MonsterFactory {
  override val spec: MonsterSpec = new MonsterSpec(
    name = "Grey Mushroom Patch",
    archetype = Mushroom,
    depth = 1,
    health = DiceRoll("1d3"),
    armorClass = 1,
    actions = actions,
    baseXp = 1,
    color = TextColors.MediumGrey
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(
        100,
        MeleeAttacksAction(Seq(new SporeAttack(DiceRoll("1d4"), effectFactory = Some(Confusion(DiceRoll("1d4"))))))
      )
    ),
    otherwise = Seq(Weighted(100, MonsterPassAction))
  )
}
