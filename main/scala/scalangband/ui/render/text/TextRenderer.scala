package scalangband.ui.render.text

import scalangband.bridge.rendering.TextColors.*
import scalangband.data.item.money.Money
import scalangband.model.item.*
import scalangband.model.level.DungeonLevel
import scalangband.model.monster.*
import scalangband.model.player.Player
import scalangband.model.tile.*
import scalangband.ui.render.{RenderableTile, Renderer}

import scala.swing.Font

class TextRenderer(font: Font) extends Renderer {
  override def render(level: DungeonLevel): Array[Array[RenderableTile]] = {
    level.tiles.map(row => renderRow(row))
  }

  private def renderRow(row: Array[Tile]): Array[RenderableTile] = {
    row.map(render)
  }

  def render(tile: Tile): RenderableTile = {
    if (tile.isVisible || tile.seen) {
      tile.representation match {
        case p: Player =>
          val color = p.healthPercent match
            case x if x <= 25 => Red
            case x if x <= 50 => Orange
            case x if x <= 75 => Yellow
            case _ => White
          TextTile ('@', font.deriveFont(java.awt.Font.BOLD), color)

        // terrain
        case _: DownStairs => TextTile('>', font, VeryLightGrey)
        case _: UpStairs => TextTile('<', font, VeryLightGrey)
        case floor: Floor if floor.isVisible => TextTile('.', font, VeryLightGrey)
        case _: Floor => TextTile(' ', font, Black)
        case _: ClosedDoor => TextTile('+', font, Brown)
        case _: OpenDoor => TextTile('\'', font, Brown)
        case _: BrokenDoor => TextTile('\'', font, Brown)
        case _: RemovableWall => TextTile('#', font, MediumGrey)
        case _: PermanentWall => TextTile('#', font, Turquoise)

        // monsters
        case monster: Monster => renderMonster(monster, font)

        // items
        case PileOfItems => TextTile('&', font, White)
        case item: Item => renderItem(item, font)

        // money
        case m: Money => TextTile('$', font, m.color)

        // oops!
        case _ => TextTile('0', font, Red)
      }
    } else TextTile(' ', font, Black)
  }
  
  private def renderMonster(monster: Monster, font: Font): TextTile = monster.archetype match {
    case Ant => TextTile('a', font, monster.color)
    case Bat => TextTile('b', font, monster.color)
    case Bird => TextTile('B', font, monster.color)
    case Centipede => TextTile('c', font, monster.color)
    case IckyThing => TextTile('i', font, monster.color)
    case Kobold => TextTile('k', font, monster.color)
    case Mold => TextTile('m', font, monster.color)
    case Mushroom => TextTile(',', font, monster.color)
    case Person => TextTile('p', font, monster.color)
    case Reptile => TextTile('R', font, monster.color)
    case Snake => TextTile('S', font, monster.color)
  }

  private def renderItem(item: Item, font: Font): TextTile = item.archetype match {
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
