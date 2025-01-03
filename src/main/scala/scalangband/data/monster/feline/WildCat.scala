package scalangband.data.monster.feline

import scalangband.bridge.rendering.TextColors.LightUmber
import scalangband.model.monster.action.{HearingBoundedAction, MeleeAttacks, MonsterActions, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.ClawAttack
import scalangband.model.monster.{BashesDoors, Feline, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object WildCat extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Wild Cat",
    archetype = Feline,
    depth = 2,
    speed = 40,
    health = DiceRoll("3d5"),
    hearing = 40,
    armorClass = 14,
    sleepiness = 0,
    experience = 8,
    doors = BashesDoors,
    actions = actions,
    color = LightUmber
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, new MeleeAttacks(Seq(new ClawAttack(DiceRoll("1d3")), new ClawAttack(DiceRoll("1d3")))))
    ),
    los = Seq(Weighted(100, PathfindingAction)),
    otherwise = Seq(Weighted(100, HearingBoundedAction(PathfindingAction, RandomMovementAction)))
  )
}
