package scalangband.model.player

import scalangband.model.item.weapon.Weapon

class Equipment(var weapon: Option[Weapon] = None) {
  override def toString: String = s"{ weapoon: $weapon }"
}