package scalangband.ui

import scala.swing.event.ButtonClicked
import scala.swing.*

class MainWindow extends MainFrame {
  title = "Scalangband"

  private val banner = new Label("Welcome to Scalangband!") {
    horizontalAlignment = Alignment.Center
  }

  private val newGameButton = new Button("New Game")
  private val quitButton = new Button("Quit")

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
