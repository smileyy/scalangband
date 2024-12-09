package scalangband

import scalangband.model.Game
import scalangband.ui.render.text.TextRenderer
import scalangband.ui.{GameWindow, MainWindow}

import scala.swing.SwingApplication

object Scalangband extends SwingApplication {
  private val mainWindow = new MainWindow()
  private var maybeGameWindow: Option[GameWindow] = None

  override def startup(args: Array[String]): Unit = {
    mainWindow.centerOnScreen()
    mainWindow.visible = true
  }

  def startGame(game: Game): Unit = {
    maybeGameWindow = Some(GameWindow(game, TextRenderer.default))
    maybeGameWindow.get.pack()
    maybeGameWindow.get.visible = true
  }

  def endGame(): Unit = {
    maybeGameWindow.get.close()
    maybeGameWindow = None
    mainWindow.visible = true
  }
}