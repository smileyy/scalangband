package scalangband.data.item.potion

import scalangband.bridge.actionresult.ActionResult
import scalangband.model.effect.Confusion
import scalangband.model.item.{Item, ItemQuality, NormalQuality}
import scalangband.model.item.potion.{Potion, PotionEffect, PotionFactory, PotionSpec}
import scalangband.model.player.PlayerCallback

import scala.util.Random

object CureLightWoundsPotion extends PotionFactory {
  override def apply(random: Random = new Random(), quality: ItemQuality = NormalQuality): Item = new Potion(spec)

  override def levels: Range = 1 to 15
  override def commonness: Int = 80

  override def spec: PotionSpec = PotionSpec(
    name = "Cure Light Wounds",
    effect = CureLightWoundsEffect
  )
}

object CureLightWoundsEffect extends PotionEffect {
  override def onQuaff(callback: PlayerCallback): List[ActionResult] = {
    var results: List[ActionResult] = List.empty

    results = callback.heal(20) :: results
    results = callback.reduceEffect(Confusion, 20) :: results

    results
  }
}
