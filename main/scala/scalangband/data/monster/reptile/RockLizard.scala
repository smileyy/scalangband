package scalangband.data.monster.reptile

import scalangband.bridge.rendering.TextColors.LightUmber
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{MonsterFactory, MonsterSpec, Reptile}
import scalangband.model.util.{DiceRoll, Weighted}

object RockLizard extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Rock Lizard",
    archetype = Reptile,
    depth = 1,
    health = DiceRoll("4d3"),
    armorClass = 4,
    experience = 2,
    sleepiness = 15,
    actions = actions,
    color = LightUmber
  )

  private def actions = new MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacksAction(new BiteAttack(DiceRoll("1d1"))))),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
