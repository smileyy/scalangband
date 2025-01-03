package scalangband.data.monster.kobold

import scalangband.bridge.rendering.TextColors.LightGreen
import scalangband.model.monster.action.{HearingBoundedAction, MeleeAttacks, MonsterActions, MonsterPassAction, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.PlainAttack
import scalangband.model.monster.{ArmoryInventoryGenerator, MonsterFactory, MonsterSpec, ProbabilisticInventoryGenerator, Kobold as KoboldArchetype}
import scalangband.model.util.{DiceRoll, Weighted}

object Kobold extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Kobold", 
    archetype = KoboldArchetype, 
    depth = 2, 
    health = DiceRoll("6d3"), 
    hearing = 20, 
    armorClass = 24, 
    sleepiness = 70, 
    experience = 5, 
    actions = actions, 
    inventory = inventory, 
    color = LightGreen
  )

  private def actions = MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacks(new PlainAttack(DiceRoll("1d8"))))),
    los = Seq(Weighted(100, PathfindingAction)),
    otherwise = Seq(Weighted(100, HearingBoundedAction(PathfindingAction, MonsterPassAction)))
  )

  private def inventory = Seq(
    new ProbabilisticInventoryGenerator(60, ArmoryInventoryGenerator(2))
  )
}
