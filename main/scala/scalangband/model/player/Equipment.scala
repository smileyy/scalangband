package scalangband.model.player

import scalangband.model.item.EquippableItem
import scalangband.model.item.armor.BodyArmor
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.weapon.Weapon
import scalangband.model.player.StatBonus.NoBonus

class Equipment(
    var weapon: Option[Weapon] = None,
    var light: Option[LightSource] = None,
    var body: Option[BodyArmor] = None
) {

  def allEquipment: Seq[EquippableItem] = Seq(weapon, light, body).flatten
  def statBonus: StatBonus = allEquipment.map(_.statBonus).foldLeft(NoBonus)((acc, bonus) => acc + bonus)

  override def toString: String = s"{ weapon: $weapon, light: $light, body: $body }"
}
