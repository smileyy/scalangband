package scalangband.model.player

import scalangband.model.player.playerclass.PlayerClass
import scalangband.model.player.race.Race

case class PlayerLevel(hitpoints: Int)
object PlayerLevel {
  def first(race: Race, cls: PlayerClass): PlayerLevel = {
    PlayerLevel(race.hitdice.max + cls.hitdice.max)
  }
  
  def next(race: Race, cls: PlayerClass): PlayerLevel = {
    PlayerLevel(race.hitdice.roll() + cls.hitdice.roll())
  }
}
