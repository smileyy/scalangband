package scalangband.ui

import scalangband.Scalangband
import scalangband.model.level.LevelGenerator
import scalangband.model.settings.Settings
import scalangband.model.{Game, Player}

import scala.swing.event.ButtonClicked
import scala.swing.{Alignment, BoxPanel, Button, Frame, GridPanel, Label, Orientation, TextField}
import scala.util.Random

class NewGameWindow() extends Frame {
  val nameLabel = new Label("Name:", null, Alignment.Right)
  val nameTextBox = new TextField(18)

  val seedLabel = new Label("Seed (optional)", null, Alignment.Right)
  val seedTextBox = new TextField(18)

  val startGameButton = new Button("Start Game")

  val fieldsPanel = new GridPanel(2, 2)
  fieldsPanel.contents += nameLabel
  fieldsPanel.contents += nameTextBox
  fieldsPanel.contents += seedLabel
  fieldsPanel.contents += seedTextBox


  contents = new BoxPanel(Orientation.Vertical) {
    contents += fieldsPanel
    contents += startGameButton
  }

  resizable = false

  listenTo(startGameButton)
  
  reactions += {
    case ButtonClicked(`startGameButton`) => 
      this.close()
      
      val seed: Long = 
        if (seedTextBox.text != "") {
          seedTextBox.text.toLong
        } else {
          Random.nextLong()
        }

      val random = new Random(seed)
      
      println(s"Starting game with seed $seed")
      
      val game = Game.newGame(seed, random, new Settings(), new Player(nameTextBox.text))
      Scalangband.startGame(game)
  }
  
  this.pack()
}