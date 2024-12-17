package scalangband.data.monster.kobold

import scalangband.bridge.rendering.TextColors.Yellow
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.PlainAttack
import scalangband.model.monster.*
import scalangband.model.util.{DiceRoll, Weighted}

object SmallKobold extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Small Kobold",
    archetype = Kobold,
    depth = 1,
    health = DiceRoll("4d3"),
    armorClass = 24,
    experience = 5,
    sleepiness = 70,
    actions = actions,
    inventory = inventory,
    color = Yellow
  )

  private def actions = new MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacksAction(new PlainAttack(DiceRoll("1d5"))))),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )

  private def inventory = Seq(
    new ProbabilisticInventoryGenerator(60, ArmoryInventoryGenerator(1))
  )
}
