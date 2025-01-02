package scalangband.data.monster.kobold

import scalangband.bridge.rendering.TextColors.Yellow
import scalangband.model.monster.*
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.PlainAttack
import scalangband.model.util.{DiceRoll, Weighted}

object SmallKobold extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Small Kobold",
    archetype = Kobold,
    depth = 1,
    health = DiceRoll("4d3"),
    hearing = 20,
    armorClass = 24,
    sleepiness = 70,
    experience = 5,
    actions = actions,
    inventory = inventory,
    color = Yellow
  )

  private def actions = MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacksAction(new PlainAttack(DiceRoll("1d5"))))),
    otherwise = Seq(Weighted(100, PathfindingAction))
  )

  private def inventory = Seq(
    new ProbabilisticInventoryGenerator(60, ArmoryInventoryGenerator(1))
  )
}
