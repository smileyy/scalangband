package scalangband.ui

import org.slf4j.LoggerFactory
import scalangband.Scalangband
import scalangband.data.item.weapon.Dagger
import scalangband.model.Game
import scalangband.model.effect.Effect
import scalangband.model.location.Coordinates
import scalangband.model.player.playerclass.{PlayerClass, Warrior}
import scalangband.model.player.race.{Human, Race}
import scalangband.model.player.{Effects, Equipment, Inventory, Player, Skills}
import scalangband.model.settings.Settings
import scalangband.ui.NewGameWindow.Logger

import scala.swing.*
import scala.swing.event.ButtonClicked
import scala.util.Random

class NewGameWindow extends Frame {
  private val nameLabel = new Label("Name:", null, Alignment.Right)
  private val nameTextBox = new TextField(18)

  private val raceLabel = new Label("Race:", null, Alignment.Right)
  private val raceMenu = new ComboBox[String](Seq("Human"))

  private val classLabel = new Label("Class:", null, Alignment.Right)
  private val classMenu = new ComboBox[String](Seq("Warrior"))

  private val seedLabel = new Label("Seed (optional):", null, Alignment.Right)
  private val seedTextBox = new TextField(18)

  private val startGameButton = new Button("Start Game")

  private val fieldsPanel = new BoxPanel(Orientation.Vertical) {
    contents += new GridPanel(4, 2) {
      contents += nameLabel
      contents += nameTextBox

      contents += raceLabel
      contents += raceMenu

      contents += classLabel
      contents += classMenu

      contents += seedLabel
      contents += seedTextBox
    }
  }

  contents = new BoxPanel(Orientation.Vertical) {
    contents += fieldsPanel
    contents += startGameButton
  }

  resizable = false

  // Event Listeners
  listenTo(startGameButton)

  reactions += {
    case ButtonClicked(`startGameButton`) =>
      this.close()

      val seed: Long = if (seedTextBox.text.nonEmpty) {
        seedTextBox.text.toLong
      } else {
        Random.nextLong()
      }
      Logger.info(s"Starting game with seed $seed")

      startNewGame(seed)
  }

  private def startNewGame(seed: Long): Unit = {
    val random = new Random(seed)

    val name = nameTextBox.text
    val cls = Warrior

    val player = Player(random, name, race, cls)

    val game = Game.newGame(seed, random, new Settings(), player)
    Scalangband.startGame(game)
  }

  private def race: Race = raceMenu.selection.item match {
    case "Human" => Human
  }

  private def cls: PlayerClass = classMenu.selection.item match {
    case "Warrior" => Warrior
  }

  this.pack()
}
object NewGameWindow {
  private val Logger = LoggerFactory.getLogger(this.getClass)
}