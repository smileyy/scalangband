package scalangband.ui.render.text

import scalangband.bridge.rendering.TextColors.*
import scalangband.model.Representable
import scalangband.model.item.*
import scalangband.model.level.DungeonLevel
import scalangband.model.monster.*
import scalangband.model.player.Player
import scalangband.model.tile.*
import scalangband.ui.render.{RenderableTile, Renderer}

import scala.swing.Font

class TextRenderer(font: Font) extends Renderer {
  override def render(player: Player, level: DungeonLevel): Array[Array[RenderableTile]] = {
    level.tiles.map(row => renderRow(player, row))
  }

  private def renderRow(player: Player, row: Array[Tile]): Array[RenderableTile] = {
    row.map(tile => render(player, tile))
  }

  private def render(player: Player, tile: Tile): RenderableTile = {
    if (tile.isVisible) {
      render(tile.representation, player)
    } else if (tile.seen) {
      tile match {
        case f: Floor => TextTile('.', font, MediumGrey)
        case _: DownStairs => TextTile('>', font, White)
        case _: UpStairs => TextTile('<', font, White)
        case _: ClosedDoor => TextTile('+', font, Brown)
        case _: OpenDoor => TextTile('\'', font, Brown)
        case _: BrokenDoor => TextTile('\'', font, Brown)
        case _: RemovableWall => TextTile('#', font, MediumGrey)
        case _: PermanentWall => TextTile('#', font, Turquoise)
      }
    }  else TextTile(' ', font, Black)
  }

  private def render(representations: Seq[Representable], player: Player): TextTile = {
    representations.head match {
      case p: Player =>
        val color = p.healthPercent match
          case x if x <= 25 => Red
          case x if x <= 50 => Orange
          case x if x <= 75 => Yellow
          case _ => White
        TextTile('@', font.deriveFont(java.awt.Font.BOLD), color)

      // monsters
      case monster: Monster => renderMonster(monster, player, representations.tail)

      // terrain
      case f: Floor => TextTile('.', font, VeryLightGrey)
      case _: DownStairs => TextTile('>', font, White)
      case _: UpStairs => TextTile('<', font, White)
      case _: ClosedDoor => TextTile('+', font, Brown)
      case _: OpenDoor => TextTile('\'', font, Brown)
      case _: BrokenDoor => TextTile('\'', font, Brown)
      case _: RemovableWall => TextTile('#', font, VeryLightGrey)
      case _: PermanentWall => TextTile('#', font, Turquoise)

      // items
      case PileOfItems => TextTile('&', font, White)
      case item: Item => renderItem(item)

      // oops!
      case _ => TextTile('0', font, Red)
    }
  }

  private def renderMonster(monster: Monster, player: Player, representations: Seq[Representable]): TextTile = {
    if (!monster.invisible || player.canSeeInvisible) {

      val color = if (monster.clear) monster.color else monster.color

      monster.archetype match {
        case Ant => TextTile('a', font, color)
        case Bat => TextTile('b', font, color)
        case Bird => TextTile('B', font, color)
        case Centipede => TextTile('c', font, color)
        case Eye => TextTile('e', font, color)
        case IckyThing => TextTile('i', font, color)
        case Kobold => TextTile('k', font, color)
        case Mold => TextTile('m', font, color)
        case Mushroom => TextTile(',', font, color)
        case Person => TextTile('p', font, color)
        case Reptile => TextTile('R', font, color)
        case Rodent => TextTile('r', font, color)
        case Snake => TextTile('S', font, color)
        case Worm => TextTile('w', font, color)
      }
    } else {
      render(representations, player)
    }
  }

  private def renderItem(item: Item): TextTile = item.archetype match {
    case SoftBodyArmor => TextTile('(', font, item.color)
    case LightSource => TextTile('~', font, item.color)
    case Miscellaneous => TextTile('~', font, item.color)
    case MoneyArchetype => TextTile('$', font, item.color)
    case Sword => TextTile('|', font, item.color)
  }
} 
object TextRenderer {
  def default: Renderer = new TextRenderer(Font(Font.Monospaced, Font.Plain, 12))
}
