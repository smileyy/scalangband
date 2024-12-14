package scalangband.ui

import scalangband.model.Game
import scalangband.ui.gamepanel.GamePanel
import scalangband.ui.render.Renderer

import scala.swing.Frame

class GameWindow(game: Game, panel: GamePanel) extends Frame {
  title = "Scalangband"

  contents = panel
  pack()
}
object GameWindow {
  def apply(game: Game, renderer: Renderer): GameWindow = {
    new GameWindow(game, GamePanel(game, renderer))
  }
}
