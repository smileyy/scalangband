package scalangband.data.monster.canine

import scalangband.bridge.rendering.TextColors.LightUmber
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{Canine, MonsterFactory, MonsterFactoryFriendSpec, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object WildDog extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Wild Dog",
    archetype = Canine,
    depth = 1,
    health = DiceRoll("1d5"),
    hearing = 10,
    armorClass = 3,
    sleepiness = 10,
    experience = 1,
    actions = actions,
    friends = Seq(MonsterFactoryFriendSpec(100, DiceRoll("2d7"), this)),
    color = LightUmber
  )

  private def actions = MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacksAction(new BiteAttack(DiceRoll("1d1"))))),
    otherwise = Seq(Weighted(100, PathfindingAction))
  )
}
