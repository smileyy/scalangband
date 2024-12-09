package scalangband.model.monster.person

import scalangband.model.item.ItemGenerator
import scalangband.model.item.garbage.GarbageGenerator
import scalangband.model.item.money.MoneyGenerator
import scalangband.model.monster.action.{MonsterAction, MonsterPassAction, RandomMovementAction, SpeakAction}
import scalangband.model.monster.{MonsterFactory, MonsterInventoryGenerator, MonsterSpec, Person}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object RandomlyMumblingTownsperson extends MonsterFactory {
  override def spec: MonsterSpec = new MonsterSpec(
    name = "Randomly Mumbling Townsperson",
    level = 0,
    archetype = Person,
    health = DiceRoll("1d4"),
    armorClass = 1,
    actions = actions,
    inventory = Some(inventory),
    color = TextColors.White
  )

  def actions: Seq[Weighted[MonsterAction]] = Seq(
    Weighted(90, MonsterPassAction),
    Weighted(9, RandomMovementAction),
    Weighted(1, SpeakAction("The townsperson mumbles incoherently."))
  )

  def inventory: MonsterInventoryGenerator =
    new MonsterInventoryGenerator(1, 2, generators)

  def generators: Seq[Weighted[ItemGenerator]] = Seq(
    Weighted(1, GarbageGenerator),
    Weighted(1, MoneyGenerator)
  )
}