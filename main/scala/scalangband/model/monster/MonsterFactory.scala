package scalangband.model.monster

import scalangband.model.Game
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.item.Item
import scalangband.model.location.Coordinates

trait MonsterFactory {
  def apply(coordinates: Coordinates): Monster = {
    Monster(spec, coordinates)
  }
  
  def spec: MonsterSpec
}