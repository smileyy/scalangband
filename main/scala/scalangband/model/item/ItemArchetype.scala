package scalangband.model.item

trait ItemArchetype

trait WeaponArchetype extends ItemArchetype
trait ArmorArchetype extends ItemArchetype

object LightSource extends ItemArchetype
object Miscellaneous extends ItemArchetype
object MoneyArchetype extends ItemArchetype
object SoftBodyArmor extends ArmorArchetype
object Sword extends WeaponArchetype
