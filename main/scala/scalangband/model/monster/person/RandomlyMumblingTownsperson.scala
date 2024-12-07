package scalangband.model.monster.person

import scalangband.model.action.player.{PlayerAction, PlayerPassAction}
import scalangband.model.action.monster.{MonsterAction, MonsterPassAction, RandomMovementAction, TauntAction}
import scalangband.model.item.Item
import scalangband.model.item.garbage.{GarbageGenerator, PotteryShard}
import scalangband.model.location.Coordinates
import scalangband.model.monster.{Monster, MonsterFactory, MonsterInventoryGenerator, MonsterSpec, Person}
import scalangband.model.util.{DiceRoll, Weighted}

import scala.collection.mutable.ListBuffer

object RandomlyMumblingTownsperson extends MonsterFactory {
  override def spec: MonsterSpec = new MonsterSpec(
    name = "Randomly Mumbling Townsperson",
    level = 0,
    archetype = Person,
    health = DiceRoll("1d4"),
    actions = actions,
    inventory = inventory
  )

  def actions: Seq[Weighted[MonsterAction]] = Seq(
    Weighted(MonsterPassAction, 90),
    Weighted(RandomMovementAction, 9),
    Weighted(TauntAction("The townsperson mumbles incoherently."), 1)
  )

  def inventory: MonsterInventoryGenerator = new MonsterInventoryGenerator(1, Seq(Weighted(GarbageGenerator, 1)))
}