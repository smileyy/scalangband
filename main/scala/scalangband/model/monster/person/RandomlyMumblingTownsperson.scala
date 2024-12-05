package scalangband.model.monster.person

import scalangband.model.action.{GameAction, PassAction, RandomMovementAction, TauntAction}
import scalangband.model.item.Item
import scalangband.model.item.garbage.PotteryShard
import scalangband.model.location.Coordinates
import scalangband.model.monster.{Monster, MonsterFactory}
import scalangband.model.util.Weighted

import scala.collection.mutable.ListBuffer

class RandomlyMumblingTownsperson(coords: Coordinates, inventory: Seq[Item]) extends Monster("Randomly Mumbling Townsperson", coords, health = 2, ListBuffer.from(inventory)) {
  override def actions: Seq[Weighted[GameAction]] = Seq(
    Weighted(PassAction, 90),
    Weighted(RandomMovementAction(this), 9),
    Weighted(TauntAction("The townsperson mumbles incoherently"), 1)
  )
}
object RandomlyMumblingTownsperson extends MonsterFactory {
  def apply(coordinates: Coordinates): Monster = {
    createMonster(coordinates, (coordinates, inventory) => new RandomlyMumblingTownsperson(coordinates, inventory))
  }
  
  override def startingInventory: Seq[Item] = Seq(PotteryShard)
}