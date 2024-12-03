package scalangband.ui.render.text

import scalangband.model.level.Level
import scalangband.model.item.garbage.PotteryShard
import scalangband.model.monster.person.RandomlyMumblingTownsperson
import scalangband.model.player.Player
import scalangband.model.tile.*
import scalangband.ui.TextColors.*
import scalangband.ui.render.{RenderableTile, Renderer}

import scala.swing.Font

class TextRenderer(font: Font) extends Renderer {
  override def tileWidth: Int = 9
  override def tileHeight: Int = 12

  override def render(level: Level): Array[Array[RenderableTile]] = {
    level.tiles.map(row => renderRow(row))
  }

  def renderRow(row: Array[Tile]): Array[RenderableTile] = {
    row.map(render)
  }

  def render(tile: Tile): RenderableTile = {
    if (tile.isVisible || tile.seen) {
      tile.representation match {
        case _: Player => TextTile('@', font.deriveFont(java.awt.Font.BOLD), White)

        // terrain
        case _: DownStairs => TextTile('>', font, VeryLightGrey)
        case _: UpStairs => TextTile('<', font, VeryLightGrey)
        case floor: Floor if floor.isVisible => TextTile('.', font, VeryLightGrey)
        case _: Floor => TextTile(' ', font, Black)
        case _: ClosedDoor => TextTile('+', font, Brown)
        case _: OpenDoor => TextTile('\'', font, Brown)
        case _: BrokenDoor => TextTile('\'', font, Brown)
        case _: RemovableWall => TextTile('#', font, MediumGrey)
        case _: PermanentWall => TextTile('#', font, DarkGrey)

        // monsters
        case _: RandomlyMumblingTownsperson => TextTile('p', font, White)

        // items
        case PileOfItems => TextTile('&', font, White)
        case PotteryShard => TextTile('~', font, LightBeige)

        // oops!
        case _ => TextTile('0', font, Red)
      }
    } else TextTile(' ', font, Black)
  }
}
object TextRenderer {
  def default: Renderer = new TextRenderer(Font.apply(Font.Monospaced, Font.Plain, 12))
}
