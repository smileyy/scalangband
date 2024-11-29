package scalangband

import scalangband.model.Game
import scalangband.ui.render.text.TextRenderer
import scalangband.ui.{GameWindow, MainWindow}

import scala.swing.SwingApplication
import scala.util.Random

object Scalangband extends SwingApplication {
  private val mainWindow = new MainWindow()
  private var maybeGameWindow: Option[GameWindow] = None

  override def startup(args: Array[String]): Unit = {
    mainWindow.centerOnScreen()
    mainWindow.visible = true
  }

  def startGame(game: Game): Unit = {
    maybeGameWindow = Option(GameWindow(game, TextRenderer.default))
    maybeGameWindow.get.pack()
    maybeGameWindow.get.visible = true
  }
}