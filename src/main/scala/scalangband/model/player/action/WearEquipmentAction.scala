package scalangband.model.player.action

import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.item.{EquippableItem, LightSource}
import scalangband.model.item.armor.BodyArmor
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.weapon.Weapon
import scalangband.model.player.Equipment
import scalangband.model.{GameAccessor, GameCallback}

class WearEquipmentAction(item: EquippableItem, fromInventory: Boolean = true) extends PhysicalAction {

  override def apply(accessor: GameAccessor, callback: GameCallback): List[ActionResult] = {
    if (fromInventory) {
      callback.player.removeFromInventory(item)
    }
    
    val maybeUnequippedItem = callback.player.equip(item)
    val equipResult = item match {
      case w: Weapon => MessageResult(s"You are wielding $item")
      case l: LightSource => MessageResult(s"Your light source is $item")
      case b: BodyArmor => MessageResult(s"You are wearing $item")
    }

    val unequipResult = maybeUnequippedItem match {
      case Some(w: Weapon) => MessageResult(s"You were wielding $item")
      case Some(l: LightSource) => MessageResult(s"You were holding $item")
      case Some(b: BodyArmor) => MessageResult(s"You were wearing $item")
      case None => NoResult
    }

    maybeUnequippedItem match {
      case Some(item) => callback.player.addToInventory(item)
      case None =>
    }
    
    List(unequipResult, equipResult)
  }
}
