package scalangband.ui

import org.slf4j.LoggerFactory
import scalangband.Scalangband
import scalangband.data.item.weapon.Dagger
import scalangband.model.Game
import scalangband.model.effect.Effect
import scalangband.model.location.Coordinates
import scalangband.model.player.{Effects, Equipment, Inventory, Player, Skills}
import scalangband.model.settings.Settings

import scala.swing.*
import scala.swing.event.ButtonClicked
import scala.util.Random

class NewGameWindow extends Frame {
  private val logger = LoggerFactory.getLogger(this.getClass)

  // UI Components
  private val nameLabel = new Label("Name:", null, Alignment.Right)
  private val nameTextBox = new TextField(18)
  private val startGameButton = new Button("Start Game")

  // Advanced Options Components
  private val advancedOptionsToggle = new ToggleButton("Advanced Options")
  private val seedLabel = new Label("Seed:", null, Alignment.Right)
  private val seedTextBox = new TextField(18)
  private val seedPanel = new GridPanel(1, 2) {
    contents += seedLabel
    contents += seedTextBox
    visible = false // Start hidden
  }

  private val fieldsPanel = new BoxPanel(Orientation.Vertical) {
    contents += new GridPanel(1, 2) {
      contents += nameLabel
      contents += nameTextBox
    }
    contents += createAdvancedOptionsPanel()
  }

  contents = new BoxPanel(Orientation.Vertical) {
    contents += fieldsPanel
    contents += startGameButton
  }

  resizable = false

  // Event Listeners
  listenTo(startGameButton, advancedOptionsToggle)

  reactions += {
    case ButtonClicked(`advancedOptionsToggle`) =>
      seedPanel.visible = advancedOptionsToggle.selected
      this.pack() // Adjust window size dynamically

    case ButtonClicked(`startGameButton`) =>
      this.close()

      val seed: Long =
        if (advancedOptionsToggle.selected && seedTextBox.text.nonEmpty) {
          seedTextBox.text.toLong
        } else {
          Random.nextLong()
        }

      val random = new Random(seed)

      logger.info(s"Starting game with seed $seed")

      val placeholderCoordinates = Coordinates(-1, -1)

      val health = 20
      val inventory = Inventory.empty()
      val equipment = new Equipment(weapon = Some(Dagger(random, 0)))

      val player = new Player(
        nameTextBox.text,
        placeholderCoordinates,
        health = health,
        skills = Skills(),
        money = 0,
        inventory = inventory,
        equipment = equipment,
        effects = Effects.empty())

      val game = Game.newGame(seed, random, new Settings(), player)
      Scalangband.startGame(game)
  }

  this.pack()

  private def createAdvancedOptionsPanel(): BoxPanel = {
    new BoxPanel(Orientation.Vertical) {
      contents += advancedOptionsToggle
      contents += seedPanel
    }
  }
}