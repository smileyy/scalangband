package scalangband.model.monster

import scalangband.model.Legendarium
import scalangband.model.item.Armory
import scalangband.model.location.Coordinates

import scala.util.Random

trait MonsterFactory {
  def apply(random: Random, coordinates: Coordinates, armory: Armory): Monster = {
    Monster(random, this, coordinates, armory)
  }
  
  def spec: MonsterSpec
}