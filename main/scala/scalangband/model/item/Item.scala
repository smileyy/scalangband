package scalangband.model.item

import scalangband.model.Representable
import scalangband.model.item.Item.startsWithVowel
import scalangband.model.item.armor.Armor
import scalangband.model.item.lightsource.LightSource
import scalangband.model.item.weapon.Weapon

import scala.swing.Color

trait Item extends Representable {
  def name: String
  def archetype: ItemArchetype
  
  def color: Color
  
  def displayName: String = name
  
  def article: String = this match {
    case w: Weapon => if (startsWithVowel(w)) "an " else "a "
    case a: Armor => ""
    case l: LightSource => if (startsWithVowel(l)) "an " else "a "
  }

  override def toString: String = displayName
} 
object Item {
  private def startsWithVowel(item: Item): Boolean = {
    vowels.contains(item.displayName.charAt(0))
  }
  
  private val vowels: Set[Char] = Set('A', 'E', 'I', 'O', 'U')
}
