package scalangband.data.monster.jelly

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.effect.Poisoning
import scalangband.model.element.Poison
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, MonsterPassAction}
import scalangband.model.monster.attack.TouchAttack
import scalangband.model.monster.{Jelly, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object WhiteJelly extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "White Jelly",
    archetype = Jelly,
    depth = 2,
    speed = 40,
    health = DiceRoll("12d5"),
    hearing = 2,
    armorClass = 1,
    sleepiness = 99,
    experience = 10,
    actions = actions,
    color = White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(
        100,
        MeleeAttacksAction(new TouchAttack(DiceRoll("1d2"), Some(Poison), Some(Poisoning(DiceRoll("1d2")))))
      )
    ),
    los = Seq(Weighted(100, MonsterPassAction)),
    otherwise = Seq(Weighted(100, MonsterPassAction))
  )
}
