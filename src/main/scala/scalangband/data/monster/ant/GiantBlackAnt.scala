package scalangband.data.monster.ant

import scalangband.bridge.rendering.TextColors.DarkGrey
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{
  Ant,
  ArmoryInventoryGenerator,
  MonsterFactory,
  MonsterSpec,
  ProbabilisticInventoryGenerator
}
import scalangband.model.util.{DiceRoll, Weighted}

object GiantBlackAnt extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Giant Black Ant",
    archetype = Ant,
    depth = 2,
    health = DiceRoll("3d6"),
    hearing = 8,
    armorClass = 24,
    sleepiness = 80,
    experience = 8,
    actions = actions,
    color = DarkGrey
  )

  private def actions = MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacksAction(new BiteAttack(DiceRoll("1d4"))))),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )

  private def inventory = Seq(
    new ProbabilisticInventoryGenerator(25, ArmoryInventoryGenerator(2))
  )

}
