package scalangband.model.monster.person

import scalangband.model.action.monster.{MonsterAction, MonsterPassAction, RandomMovementAction, TauntAction}
import scalangband.model.item.ItemGenerator
import scalangband.model.item.garbage.GarbageGenerator
import scalangband.model.item.money.MoneyGenerator
import scalangband.model.monster.{MonsterFactory, MonsterInventoryGenerator, MonsterSpec, Person}
import scalangband.model.util.{DiceRoll, Weighted}
import scalangband.ui.TextColors

object RandomlyMumblingTownsperson extends MonsterFactory {
  override def spec: MonsterSpec = new MonsterSpec(
    name = "Randomly Mumbling Townsperson",
    depth = 0,
    archetype = Person,
    health = DiceRoll("1d4"),
    actions = actions,
    inventory = Some(inventory),
    color = TextColors.White
  )

  def actions: Seq[Weighted[MonsterAction]] = Seq(
    Weighted(MonsterPassAction, 90),
    Weighted(RandomMovementAction, 9),
    Weighted(TauntAction("The townsperson mumbles incoherently."), 1)
  )

  def inventory: MonsterInventoryGenerator =
    new MonsterInventoryGenerator(1, 2, generators)

  def generators: Seq[Weighted[ItemGenerator]] = Seq(
    Weighted(GarbageGenerator, 1),
    Weighted(MoneyGenerator, 1)
  )
}