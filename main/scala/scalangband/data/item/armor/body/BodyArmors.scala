package scalangband.data.item.armor.body

import scalangband.bridge.rendering.TextColors.{Blue, LightUmber}
import scalangband.model.item.SoftBodyArmor
import scalangband.model.item.armor.{ArmorSpec, BodyArmorFactory}

object Robe extends BodyArmorFactory {
  override val spec: ArmorSpec = ArmorSpec(
    name = "Robe",
    archetype = SoftBodyArmor,
    armorClass = 2,
    color = Blue
  )

  override val levels: Range = 1 to 100
  override val commonness: Int = 20
}

object SoftLeatherArmor extends BodyArmorFactory {
  override val spec: ArmorSpec = ArmorSpec(
    name = "Soft Leather Armor",
    archetype = SoftBodyArmor,
    armorClass = 8,
    color = LightUmber
  )

  override val levels: Range = 3 to 100
  override val commonness: Int = 20
}
