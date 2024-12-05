package scalangband.model.monster

import scalangband.model.Game
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.item.Item
import scalangband.model.location.Coordinates

trait MonsterFactory {
  def createMonster(start: Coordinates, create: (Coordinates, Seq[Item]) => Monster): Monster = {
    create(start, startingInventory)
  }
  
  def startingInventory: Seq[Item]
}