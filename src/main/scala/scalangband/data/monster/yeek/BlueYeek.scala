package scalangband.data.monster.yeek

import scalangband.bridge.rendering.TextColors.Blue
import scalangband.model.monster.action.{HearingBoundedAction, MeleeAttacksAction, MonsterActions, MonsterPassAction, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.PlainAttack
import scalangband.model.monster.*
import scalangband.model.util.{DiceRoll, Weighted}

object BlueYeek extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Blue Yeek",
    archetype = Yeek,
    depth = 2,
    health = DiceRoll("2d6"),
    hearing = 18,
    armorClass = 16,
    sleepiness = 10,
    experience = 4,
    actions = actions,
    inventory = inventory,
    color = Blue
  )

  private def actions = MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacksAction(new PlainAttack(DiceRoll("1d5"))))),
    los = Seq(Weighted(100, PathfindingAction)),
    otherwise = Seq(Weighted(100, HearingBoundedAction(PathfindingAction, MonsterPassAction)))
  )

  private def inventory = Seq(
    new ProbabilisticInventoryGenerator(60, ArmoryInventoryGenerator(2))
  )
}
