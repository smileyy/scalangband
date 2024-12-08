package scalangband.model.player

import scalangband.model.item.weapon.{Fists, Weapon}

class Equipment(var weapon: Option[Weapon] = None) {
  def getWeapon: Weapon = weapon.getOrElse(Fists)
}
