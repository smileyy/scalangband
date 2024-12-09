package scalangband.ui

import scalangband.model.Game
import scalangband.model.action.*
import scalangband.model.action.player.PlayerAction
import scalangband.model.action.result.{ActionResult, MessagesResult}
import scalangband.ui.GamePanel.{MaxMessageLineLength, PlayerPaneWidth}
import scalangband.ui.keys.{KeyHandler, MainKeyHandler}
import scalangband.ui.render.Renderer

import scala.swing.*
import scala.swing.event.KeyPressed

class GamePanel(game: Game, var renderer: Renderer, var keyHandlers: List[KeyHandler], var messages: List[String] = List.empty) extends Panel {

  private val callback = new GamePanelCallback(this)
  
  font = Font(Font.Monospaced, Font.Plain, 12)

  preferredSize = new Dimension(800, 600)
  background = new Color(0, 0, 0)

  focusable = true
  listenTo(keys)

  reactions += {
    case kp: KeyPressed =>
      if (messages.nonEmpty) {
        repaint()
      } else {
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
    case MessagesResult(messages, _) => this.messages = messages ::: this.messages
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

    val lineHeight = g.getFontMetrics(font).getHeight
    val characterPaneWidth = g.getFontMetrics(font).charWidth(' ') * (PlayerPaneWidth + 1)

    paintMessages(g, lineHeight)
    paintPlayer(g, lineHeight, lineHeight)
    paintLevel(g, lineHeight, characterPaneWidth)
    paintDepth(g, lineHeight, characterPaneWidth)
  }

  private def paintMessages(g: Graphics2D, messageLineYOffset: Int): Unit = {
    if (messages.nonEmpty) {
      g.setColor(TextColors.White)

      val line = StringBuilder()
      while (messages.nonEmpty && line.length() + 1 + messages.head.length <= MaxMessageLineLength) {
        if (line.nonEmpty) line.append(' ')
        line.append(messages.head)
        messages = messages.tail
      }

      g.drawString(line.toString, 0, messageLineYOffset)
    }
  }

  private def paintPlayer(g: Graphics2D, yOffset: Int, lineHeight: Int): Unit = {
    g.setColor(TextColors.White)

    val playerNameDisplay = if (game.player.name.length < PlayerPaneWidth) {
      game.player.name
    } else {
      game.player.name.substring(0, PlayerPaneWidth - 1)
    }

    g.drawString(playerNameDisplay, 0, yOffset + lineHeight)

    g.drawString(rightAlignedFieldString("AU", game.player.money.toString), 0, yOffset + lineHeight * 2)
    g.drawString(rightAlignedFieldString("HP", game.player.health.toString), 0, yOffset + lineHeight * 4)
  }

  private def rightAlignedFieldString(label: String, value: String) = {
    val displayString = StringBuilder(label)
    val padding = PlayerPaneWidth - displayString.length() - value.length
    displayString.append("".padTo(padding, ' '))
    displayString.append(value)

    displayString.toString()
  }

  private def paintLevel(g: Graphics2D, messageLineHeight: Int, characterPaneWidth: Int): Unit = {
    val tiles = renderer.render(game.level)

    for (rowIdx <- tiles.indices) {
      for (colIdx <- tiles(rowIdx).indices) {
        tiles(rowIdx)(colIdx).render(g, colIdx * renderer.tileWidth + characterPaneWidth, (rowIdx + 1) * renderer.tileHeight + messageLineHeight + 1)
      }
    }
  }

  private def paintDepth(g: Graphics2D, lineHeight: Int, characterPaneWidth: Int): Unit = {
    g.setColor(TextColors.White)
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