package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.item.armor.Armor
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.weapon.Weapon
import scalangband.model.player.Equipment
import scalangband.model.{GameAccessor, GameCallback}

class TakeOffEquipmentAction(f: Equipment => Option[EquippableItem]) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.unequip(f) match {
      case Some(item) =>
        callback.player.addToInventory(item)
        item match {
          case w: Weapon => List(MessageResult(s"You were wielding $item."))
          case l: LightSource => List(MessageResult(s"You were holding $item."))
          case a: Armor => List(MessageResult(s"You were wearing $item."))
        }
      case None => List.empty
    }
  }
}
