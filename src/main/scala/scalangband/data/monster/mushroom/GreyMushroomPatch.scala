package scalangband.data.monster.mushroom

import scalangband.bridge.rendering.TextColors
import scalangband.model.effect.Confusion
import scalangband.model.monster.action.{MeleeAttacks, MonsterActions, MonsterPassAction}
import scalangband.model.monster.attack.SporeAttack
import scalangband.model.monster.{MonsterFactory, MonsterSpec, Mushroom}
import scalangband.model.util.{DiceRoll, Weighted}

object GreyMushroomPatch extends MonsterFactory {
  override val spec: MonsterSpec = new MonsterSpec(
    name = "Grey Mushroom Patch",
    archetype = Mushroom,
    depth = 1,
    health = DiceRoll("1d3"),
    hearing = 2,
    armorClass = 1,
    sleepiness = 0,
    experience = 1,
    alive = false,
    actions = actions,
    color = TextColors.MediumGrey
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(
        100,
        MeleeAttacks(Seq(new SporeAttack(DiceRoll("1d4"), effectFactory = Some(Confusion(DiceRoll("1d4"))))))
      )
    ),
    los = Seq(Weighted(100, MonsterPassAction)),
    otherwise = Seq(Weighted(100, MonsterPassAction))
  )
}
