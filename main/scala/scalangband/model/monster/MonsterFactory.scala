package scalangband.model.monster

import scalangband.model.Game
import scalangband.model.Game.BaseEnergyUnit
import scalangband.model.item.Item
import scalangband.model.location.Coordinates

import scala.util.Random

trait MonsterFactory {
  def apply(coordinates: Coordinates, random: Random): Monster = {
    Monster(spec, coordinates, random)
  }
  
  def spec: MonsterSpec
}