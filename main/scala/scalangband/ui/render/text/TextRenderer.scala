package scalangband.ui.render.text

import scalangband.model.level.Level
import scalangband.model.monster.*
import scalangband.model.player.Player
import scalangband.model.tile.*
import scalangband.bridge.rendering.TextColors.*
import scalangband.data.item.garbage.PotteryShard
import scalangband.data.item.money.CopperCoins
import scalangband.ui.render.{RenderableTile, Renderer}

import scala.swing.Font

class TextRenderer(font: Font) extends Renderer {
  override def render(level: Level): Array[Array[RenderableTile]] = {
    level.tiles.map(row => renderRow(row))
  }

  def renderRow(row: Array[Tile]): Array[RenderableTile] = {
    row.map(render)
  }

  def render(tile: Tile): RenderableTile = {
    if (tile.isVisible || tile.seen) {
      tile.representation match {
        case p: Player =>
          println(p.health.percent)
          val color = p.health.percent match
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

        case PotteryShard => TextTile('~', font, LightBeige)

        // money
        case _: CopperCoins => TextTile('$', font, Brown)

        // oops!
        case _ => TextTile('0', font, Red)
      }
    } else TextTile(' ', font, Black)
  }
  
  def renderMonster(monster: Monster, font: Font): TextTile = monster.archetype match {
    case Ant => TextTile('a', font, monster.color)
    case Bird => TextTile('B', font, monster.color)
    case Centipede => TextTile('c', font, monster.color)
    case Mold => TextTile('m', font, monster.color)
    case Mushroom => TextTile(',', font, monster.color)
    case Person => TextTile('p', font, monster.color)
  }
} 
object TextRenderer {
  def default: Renderer = new TextRenderer(Font(Font.Monospaced, Font.Plain, 12))
}
