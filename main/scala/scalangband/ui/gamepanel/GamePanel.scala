package scalangband.ui.gamepanel

import scalangband.Scalangband
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.Game
import scalangband.model.player.action.PlayerAction
import scalangband.ui.*
import scalangband.ui.keys.{KeyHandler, MainKeyHandler}
import scalangband.ui.render.Renderer

import scala.swing.*
import scala.swing.event.*

class GamePanel(game: Game, renderer: Renderer, var keyHandlers: List[KeyHandler]) extends Panel {

  font = Font(Font.Monospaced, Font.Plain, 12)

  private val messagePane = new MessagePane(font)
  private val playerPane = new PlayerPane(game, font)
  private val levelPane = new LevelPane(game, renderer)

  private val callback = new GamePanelCallback(this)

  preferredSize = new Dimension(1024, 768)
  background = new Color(0, 0, 0)

  focusable = true
  listenTo(keys)

  reactions += { case kp: KeyPressed =>
    if (game.player.isDead) {
      Scalangband.endGame()
    }
    if (messagePane.messages.nonEmpty) {
      repaint()
    } else {
      repaint()
      messagePane.messages = List.empty
      keyHandlers.head.handleKeyPressed(kp, game, callback).foreach(action => dispatchAction(action))
    }
  }

  private def dispatchAction(action: PlayerAction): Unit = {
    val results = game.takeTurn(action)
    results.foreach(result => applyResult(result))
    repaint()
  }

  private def applyResult(result: ActionResult): Unit = result match {
    case MessageResult(message) => messagePane.messages = message :: messagePane.messages
    case NoResult               =>
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

    val lineHeight = g.getFontMetrics(font).getHeight
    val charWidth = g.getFontMetrics(font).charWidth(' ')

    messagePane.paint(g, 0, 0)
    playerPane.paint(g, 0, lineHeight)
    levelPane.paint(g, (PlayerPane.CharWidth + 1) * charWidth, lineHeight * 2)
  }
}
object GamePanel {
  def apply(game: Game, renderer: Renderer): GamePanel = {
    new GamePanel(game, renderer, List(MainKeyHandler))
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

