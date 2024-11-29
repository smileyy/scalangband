package scalangband.ui

import scalangband.model.Game
import scalangband.model.level.LevelGenerator

import scala.swing.event.ButtonClicked
import scala.swing.{Alignment, BoxPanel, Button, FlowPanel, Frame, GridPanel, Label, MainFrame, Orientation, Swing}
import scala.util.Random

class MainWindow() extends MainFrame {
  title = "Scalangband"

  val banner = new Label("Welcome to Scalangband!") {
    horizontalAlignment = Alignment.Center
  }

  val newGameButton = new Button("New Game")
  val quitButton = new Button("Quit")

  contents = new GridPanel(3, 1) {
    contents ++= List(banner, newGameButton, quitButton)
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  listenTo(newGameButton, quitButton)

  reactions += {
    case ButtonClicked(`newGameButton`) =>
      this.visible = false
      val newGameWindow = new NewGameWindow()
      newGameWindow.centerOnScreen()
      newGameWindow.visible = true
    case ButtonClicked(`quitButton`) => System.exit(0)
  }

  this.pack()
}
