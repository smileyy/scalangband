package scalangband.model.item

trait ItemArchetype

trait EquippableArchetype extends ItemArchetype

trait WeaponArchetype extends EquippableArchetype
trait ArmorArchetype extends EquippableArchetype

object FoodArchetype extends ItemArchetype
object LightSource extends ItemArchetype
object Miscellaneous extends ItemArchetype
object MoneyArchetype extends ItemArchetype
object Potion extends ItemArchetype
object SoftBodyArmor extends ArmorArchetype
object Sword extends WeaponArchetype
