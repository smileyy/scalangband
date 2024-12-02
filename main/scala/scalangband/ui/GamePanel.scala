package scalangband.ui

import scalangband.model.Game
import scalangband.model.action.*
import scalangband.model.action.result.{ActionResult, MessageResult}
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
        messages = messages.tail
        repaint()
      } else {
        keyHandlers.head.handleKeyPressed(kp, callback).foreach(action => dispatchAction(action))
    }
  }

  private def dispatchAction(action: GameAction): Unit = {
    val results = game.takeTurn(action)
    results.foreach(result => applyResult(result))
    repaint()
  }

  private def applyResult(result: ActionResult): Unit = result match {
    case MessageResult(messages) => this.messages = messages ::: this.messages
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

    val lineHeight = g.getFontMetrics(font).getHeight
    val characterPaneWidth = g.getFontMetrics(font).charWidth(' ') * 20

    paintMessages(g, lineHeight)
    paintPlayer(g, lineHeight)
    paintLevel(g, lineHeight, characterPaneWidth)
    paintDepth(g, characterPaneWidth, lineHeight)
  }

  private def paintMessages(g: Graphics2D, messageLineYOffset: Int): Unit = {
    if (messages.nonEmpty) {
      g.setColor(TextColors.White)

      val line = if (messages.size == 1) messages.head else messages.head + " (more)"

      g.drawString(line, 0, messageLineYOffset)
    }
  }

  private def paintPlayer(g: Graphics2D, lineHeight: Int): Unit = {
    val displayName = if (game.player.name.length < 20) {
      game.player.name
    } else {
      game.player.name.substring(0, 19)
    }

    g.setColor(TextColors.White)
    g.drawString(displayName, 0, lineHeight * 2)
  }

  private def paintLevel(g: Graphics2D, messageLineHeight: Int, characterPaneWidth: Int): Unit = {
    val tiles = renderer.render(game.level)

    for (rowIdx <- tiles.indices) {
      for (colIdx <- tiles(rowIdx).indices) {
        tiles(rowIdx)(colIdx).render(g, colIdx * renderer.tileWidth + characterPaneWidth, (rowIdx + 1) * renderer.tileHeight + messageLineHeight + 1)
      }
    }
  }

  private def paintDepth(g: Graphics2D, characterPaneWidth: Int, lineHeight: Int): Unit = {
    g.setColor(TextColors.White)
    val depth = if (game.level.depth == 0) "Town" else s"${game.level.depth * 50} feet"
    g.drawString(depth, characterPaneWidth, game.level.tiles.length * renderer.tileHeight + lineHeight * 2)
  }
}
object GamePanel {
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
}