package scalangband.data.monster.person

import scalangband.bridge.rendering.TextColors
import scalangband.data.item.garbage.GarbageGenerator
import scalangband.data.item.money.MoneyGenerator
import scalangband.model.item.ItemGenerator
import scalangband.model.monster.action.{MonsterActions, MonsterPassAction, RandomMovementAction, SpeakAction}
import scalangband.model.monster.{MonsterFactory, MonsterInventoryGenerator, MonsterSpec, Person}
import scalangband.model.util.{DiceRoll, Weighted}

object RandomlyMumblingTownsperson extends MonsterFactory {
  override def spec: MonsterSpec = new MonsterSpec(
    name = "Randomly Mumbling Townsperson",
    archetype = Person,
    depth = 0,
    health = DiceRoll("1d4"),
    armorClass = 1,
    experience = 0,
    sleepiness = 0,
    actions = actions,
    inventory = inventory,
    color = TextColors.White
  )

  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(90, MonsterPassAction),
      Weighted(9, RandomMovementAction),
      Weighted(1, SpeakAction("The townsperson mumbles incoherently."))
    ),
    otherwise = Seq(
      Weighted(90, MonsterPassAction),
      Weighted(9, RandomMovementAction),
      Weighted(1, SpeakAction("The townsperson mumbles incoherently."))
    )
  )

  def inventory: Seq[MonsterInventoryGenerator] = Seq(
    new MonsterInventoryGenerator(100, Seq(Weighted(1, GarbageGenerator))),
    new MonsterInventoryGenerator(100, Seq(Weighted(1, MoneyGenerator))),
  )
}
