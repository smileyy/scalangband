package scalangband.model.monster

import scalangband.model.location.Coordinates

import scala.util.Random

trait MonsterFactory {
  def apply(coordinates: Coordinates, random: Random): Monster = {
    Monster(spec, coordinates, random)
  }
  
  def spec: MonsterSpec
}