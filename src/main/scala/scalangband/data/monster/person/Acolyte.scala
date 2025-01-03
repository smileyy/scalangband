package scalangband.data.monster.person

import scalangband.bridge.rendering.TextColors.Green
import scalangband.model.monster.*
import scalangband.model.monster.action.spell.{FearSpell, HealSpell}
import scalangband.model.monster.action.{HearingBoundedAction, MeleeAttacks, MonsterActions, MonsterPassAction, PathfindingAction}
import scalangband.model.monster.attack.PlainAttack
import scalangband.model.util.{DiceRoll, Weighted}

object Acolyte extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Acolyte",
    archetype = Person,
    depth = 2,
    health = DiceRoll("4d7+4"),
    hearing = 20,
    armorClass = 15,
    sleepiness = 80,
    experience = 6,
    doors = OpensDoors,
    actions = actions,
    inventory = inventory,
    friends = friends,
    color = Green
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(92, MeleeAttacks(new PlainAttack(DiceRoll("1d5")))),
      Weighted(4, FearSpell(DiceRoll("1d5"))),
      Weighted(4, HealSpell(DiceRoll("2d7+2")))
    ),
    los = Seq(
      Weighted(92, PathfindingAction),
      Weighted(4, FearSpell(DiceRoll("1d5"))),
      Weighted(4, HealSpell(DiceRoll("2d7+2")))
    ),
    otherwise = Seq(Weighted(100, HearingBoundedAction(PathfindingAction, MonsterPassAction)))
  )

  private def inventory = Seq(
    new ProbabilisticInventoryGenerator(40, ArmoryInventoryGenerator(2))
  )

  private def friends = Seq(
    MonsterArchetypeFriendSpec(20, DiceRoll("1d3"), Person),
    MonsterArchetypeFriendSpec(40, DiceRoll("1d3"), Person),
    MonsterArchetypeFriendSpec(80, DiceRoll("1d3"), Person),
    MonsterFactoryFriendSpec(50, DiceRoll("1d2"), Soldier)
  ).reverse

}
