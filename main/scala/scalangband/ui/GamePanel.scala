package scalangband.ui

import scalangband.Scalangband
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.bridge.rendering.TextColors
import scalangband.bridge.rendering.TextColors.*
import scalangband.model.Game
import scalangband.model.player.{Player, Stat, Stats}
import scalangband.model.player.action.PlayerAction
import scalangband.ui.GamePanel.{MaxMessageLineLength, PlayerPaneWidth}
import scalangband.ui.keys.{KeyHandler, MainKeyHandler}
import scalangband.ui.render.Renderer

import scala.swing.*
import scala.swing.event.*

class GamePanel(
    game: Game,
    var renderer: Renderer,
    var keyHandlers: List[KeyHandler],
    var messages: List[String] = List.empty
) extends Panel {

  private val callback = new GamePanelCallback(this)

  font = Font(Font.Monospaced, Font.Plain, 12)

  preferredSize = new Dimension(800, 600)
  background = new Color(0, 0, 0)

  focusable = true
  listenTo(keys)

  reactions += { case kp: KeyPressed =>
    if (game.player.isDead) {
      Scalangband.endGame()
    }
    if (messages.nonEmpty) {
      repaint()
    } else {
      repaint()
      messages = List.empty
      keyHandlers.head.handleKeyPressed(kp, game, callback).foreach(action => dispatchAction(action))
    }
  }

  private def dispatchAction(action: PlayerAction): Unit = {
    val results = game.takeTurn(action)
    results.foreach(result => applyResult(result))
    repaint()
  }

  private def applyResult(result: ActionResult): Unit = result match {
    case MessageResult(message) => this.messages = message :: this.messages
    case NoResult               =>
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

    val lineHeight = g.getFontMetrics(font).getHeight
    val charWidth = g.getFontMetrics(font).charWidth(' ')
    val playerPanePixelWidth = charWidth * (PlayerPaneWidth + 1)

    paintMessages(g, lineHeight)
    paintPlayer(g, charWidth, lineHeight, lineHeight)
    paintLevel(g, lineHeight, playerPanePixelWidth + charWidth)
    paintDepth(g, lineHeight, playerPanePixelWidth + charWidth)
  }

  private def paintMessages(g: Graphics2D, messageLineYOffset: Int): Unit = {
    if (messages.nonEmpty) {
      g.setColor(White)

      val line = StringBuilder()
      while (messages.nonEmpty && line.length() + 1 + messages.head.length <= MaxMessageLineLength) {
        if (line.nonEmpty) line.append(' ')
        line.append(messages.head)
        messages = messages.tail
      }

      g.drawString(line.toString, 0, messageLineYOffset)
    }
  }

  private def paintPlayer(g: Graphics2D, charWidth: Int, yOffset: Int, lineHeight: Int): Unit = {
    new PlayerPane(game.player, font).paint(g, 0, lineHeight)
  }

  def drawLabeledValue(
      g: Graphics2D,
      charWidthInPixels: Int,
      totalWidthInChars: Int,
      label: String,
      value: String,
      color: Color,
      x: Int,
      y: Int
  ): Unit = {
    val valueOffset = (PlayerPaneWidth - value.length) * charWidthInPixels

    g.setColor(White)
    g.drawString(label, x, y)
    g.setColor(color)
    g.drawString(value, x + valueOffset, y)
  }
  
  private def paintLevel(g: Graphics2D, messageLineHeight: Int, characterPaneWidth: Int): Unit = {
    val tiles = renderer.render(game.level)

    for (rowIdx <- tiles.indices) {
      for (colIdx <- tiles(rowIdx).indices) {
        tiles(rowIdx)(colIdx).render(
          g,
          colIdx * renderer.tileWidth + characterPaneWidth,
          (rowIdx + 1) * renderer.tileHeight + messageLineHeight + 1
        )
      }
    }
  }

  private def paintDepth(g: Graphics2D, lineHeight: Int, characterPaneWidth: Int): Unit = {
    g.setColor(White)
    val depth = if (game.level.depth == 0) "Town" else s"${game.level.depth * 50} feet"
    g.drawString(depth, characterPaneWidth, game.level.tiles.length * renderer.tileHeight + lineHeight * 2)
  }
}
object GamePanel {
  private val MaxMessageLineLength = 96
  private val PlayerPaneWidth = 12

  def apply(game: Game, renderer: Renderer): GamePanel = {
    new GamePanel(game, renderer, List(MainKeyHandler), List.empty)
  }
}

class GamePanelCallback(panel: GamePanel) {
  def pushKeyHandler(handler: KeyHandler): Unit = {
    panel.keyHandlers = handler :: panel.keyHandlers
  }

  def popKeyHandler(): Unit = {
    panel.keyHandlers = panel.keyHandlers.tail
  }

  def repaint(): Unit = {
    panel.repaint()
  }
}

class PlayerPane(player: Player, font: Font) {
  def paint(g: Graphics2D, x: Int, y: Int): Unit = {
    val lineHeight = g.getFontMetrics(font).getHeight
    val charWidth = g.getFontMetrics(font).charWidth(' ')

    g.setColor(White)
    g.drawString(playerNameDisplay, x, y + lineHeight)

    g.setColor(Turquoise)
    g.drawString(player.race.name, x, y + lineHeight * 2)
    g.drawString(player.cls.name, x, y + lineHeight * 3)

    new LevelField().paint(player, g, font, 0, 5, PlayerPane.CharWidth)
    new MoneyField().paint(player, g, font, 0, 7, PlayerPane.CharWidth)

    new StatField("STR", stats => stats.str).paint(player, g, font, 0, 9, PlayerPane.CharWidth)
    new StatField("INT", stats => stats.intg).paint(player, g, font, 0, 10, PlayerPane.CharWidth)
    new StatField("WIS", stats => stats.wis).paint(player, g, font, 0, 11, PlayerPane.CharWidth)
    new StatField("DEX", stats => stats.dex).paint(player, g, font, 0, 12, PlayerPane.CharWidth)
    new StatField("CON", stats => stats.con).paint(player, g, font, 0, 13, PlayerPane.CharWidth)

    new ArmorClassField().paint(player, g, font, 0, 15, PlayerPane.CharWidth)
    new HealthField().paint(player, g, font, 0, 16, PlayerPane.CharWidth)
  }

  private def playerNameDisplay = {
    if (player.name.length < PlayerPane.CharWidth) {
      player.name
    } else {
      player.name.substring(0, PlayerPane.CharWidth - 1)
    }
  }

}
object PlayerPane {
  def CharWidth: Int = 12
}

trait LabeledField {
  def paint(player: Player, g: Graphics2D, font: Font, x: Int, y: Int, width: Int): Unit = {
    val charWidth = g.getFontMetrics(font).charWidth(' ')
    val charHeight = g.getFontMetrics(font).getHeight

    val value = getValue(player)
    val valueOffset = width - value.length

    g.setColor(labelColor)
    g.drawString(label, x * charWidth, (y + 1) * charHeight)
    g.setColor(valueColor)
    g.drawString(value, (x + valueOffset) * charWidth, (y + 1) * charHeight)
  }

  def label: String
  def getValue(player: Player): String
  def labelColor: Color = White
  def valueColor: Color = Green
}

class LevelField extends LabeledField {
  override def label: String = "Level"
  override def getValue(player: Player): String = player.level.toString
}

class MoneyField extends LabeledField {
  override def label: String = "AU"
  override def getValue(player: Player): String = player.money.toString
}

class StatField(statName: String, getStat: Stats => Stat) extends LabeledField {
  override def label: String = s"$statName:"
  override def getValue(player: Player): String = getStat(player.stats).toString
}

class ArmorClassField extends LabeledField {
  override def label: String = "AC"
  override def getValue(player: Player): String = player.armorClass.toString
}

class HealthField extends LabeledField {
  override def label: String = "HP"
  override def getValue(player: Player): String = player.health.toString
}