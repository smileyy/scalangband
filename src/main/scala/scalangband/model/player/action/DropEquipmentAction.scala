package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessageResult}
import scalangband.model.item.{EquippableItem, Item}
import scalangband.model.item.armor.Armor
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.weapon.Weapon
import scalangband.model.player.Equipment
import scalangband.model.{GameAccessor, GameCallback}

class DropEquipmentAction(unequip: Equipment => Option[EquippableItem]) extends PhysicalAction {
  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    callback.player.unequip(unequip) match {
      case Some(item) =>
        println(item)

        var results: List[ActionResult] = List.empty

        val placeItemResult = callback.level.addItemToTile(accessor.player.coordinates, item)

        val unequipResult = item match {
          case w: Weapon      => MessageResult(s"You were wielding $item.")
          case l: LightSource => MessageResult(s"You were holding $item.")
          case a: Armor       => MessageResult(s"You were wearing $item.")
        }

        List(placeItemResult, MessageResult(s"You drop $item."), unequipResult)
      case None =>
        println("No item for some reason")
        List.empty
    }
  }
}
