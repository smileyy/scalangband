package scalangband.ui

import org.slf4j.LoggerFactory
import scalangband.Scalangband
import scalangband.model.location.Coordinates
import scalangband.model.settings.Settings
import scalangband.model.Game
import scalangband.model.player.{Inventory, Player}

import scala.swing.event.ButtonClicked
import scala.swing.*
import scala.util.Random

class NewGameWindow extends Frame {
  private val logger = LoggerFactory.getLogger(this.getClass)

  private val nameLabel = new Label("Name:", null, Alignment.Right)
  private val nameTextBox = new TextField(18)

  private val seedLabel = new Label("Seed (optional)", null, Alignment.Right)
  private val seedTextBox = new TextField(18)

  private val startGameButton = new Button("Start Game")

  private val fieldsPanel = new GridPanel(2, 2)
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

      logger.info(s"Starting game with seed $seed")
      
      val placeholderCoordinates = Coordinates(-1, -1)
      val player = new Player(nameTextBox.text, placeholderCoordinates, inventory = Inventory.empty())
      val game = Game.newGame(seed, random, new Settings(), player)
      Scalangband.startGame(game)
  }
  
  this.pack()
}