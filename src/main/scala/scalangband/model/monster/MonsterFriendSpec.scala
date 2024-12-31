package scalangband.model.monster

import scalangband.model.util.DiceRoll

sealed trait MonsterFriendSpec {
  def probability: Int
  def number: DiceRoll
}

class MonsterFactoryFriendSpec(val probability: Int, val number: DiceRoll, val factory: MonsterFactory)
  extends MonsterFriendSpec

class MonsterArchetypeFriendSpec(val probability: Int, val number: DiceRoll, val archetype: MonsterArchetype)
  extends MonsterFriendSpec