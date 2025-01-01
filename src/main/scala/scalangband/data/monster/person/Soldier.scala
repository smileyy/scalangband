package scalangband.data.monster.person

import scalangband.bridge.rendering.TextColors.Umber
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, PathfindingAction, RandomMovementAction}
import scalangband.model.monster.attack.PlainAttack
import scalangband.model.monster.{
  ArmoryInventoryGenerator,
  MonsterArchetypeFriendSpec,
  MonsterFactory,
  MonsterFactoryFriendSpec,
  MonsterSpec,
  OpensDoors,
  Person,
  ProbabilisticInventoryGenerator
}
import scalangband.model.util.{DiceRoll, Weighted}

object Soldier extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Soldier",
    archetype = Person,
    depth = 2,
    health = DiceRoll("7d6"),
    hearing = 20,
    armorClass = 24,
    sleepiness = 80,
    experience = 6,
    doors = OpensDoors,
    actions = actions,
    inventory = inventory,
    friends = friends,
    color = Umber
  )

  private def actions = MonsterActions(
    adjacent =
      Seq(Weighted(100, MeleeAttacksAction(Seq(new PlainAttack(DiceRoll("1d7")), new PlainAttack(DiceRoll("1d7")))))),
    otherwise = Seq(Weighted(100, PathfindingAction))
  )

  private def inventory = Seq(
    new ProbabilisticInventoryGenerator(60, ArmoryInventoryGenerator(2))
  )

  private def friends = Seq(
    MonsterArchetypeFriendSpec(20, DiceRoll("1d3"), Person),
    MonsterArchetypeFriendSpec(40, DiceRoll("1d3"), Person),
    MonsterArchetypeFriendSpec(80, DiceRoll("1d3"), Person),
    MonsterFactoryFriendSpec(50, DiceRoll("1d2"), Soldier)
  ).reverse
}
