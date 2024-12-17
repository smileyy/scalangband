package scalangband.ui.gamepanel

import scalangband.model.Game
import scalangband.ui.render.Renderer

import scala.swing.Graphics2D

class LevelPane(game: Game, renderer: Renderer) {
  def paint(g: Graphics2D, x: Int, y: Int): Unit = {
    val tiles = renderer.render(game.player, game.level)

    for (row <- tiles.indices) {
      for (col <- tiles(row).indices) {
        tiles(row)(col).render(g, x, y, col, row)
      }
    }

  }
}