package scalangband.ui.gamepanel

import scalangband.Scalangband
import scalangband.bridge.actionresult.{ActionResult, MessageResult, NoResult}
import scalangband.model.Game
import scalangband.model.player.action.PlayerAction
import scalangband.ui.*
import scalangband.ui.gamepanel.overlay.GamePanelOverlay
import scalangband.ui.keys.MainKeyHandler
import scalangband.ui.render.Renderer

import scala.swing.*
import scala.swing.event.*

class GamePanel(game: Game, renderer: Renderer, var maybeOverlay: Option[GamePanelOverlay] = None) extends Panel {

  font = Font(Font.Monospaced, Font.Plain, 12)

  private val messagePane = new MessagePane(font)
  private val playerPane = new PlayerPane(game, font)
  private val levelPane = new LevelPane(game, renderer)

  preferredSize = new Dimension(GamePanel.WidthInPixels, GamePanel.HeightInPixels)
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
      messagePane.messages = List.empty
      maybeOverlay.map(overlay => overlay.keyHandler).getOrElse(MainKeyHandler).handleKeyPressed(kp, game) match {
        case Left(None) =>
          maybeOverlay = None
          repaint()
        case Left(Some(action)) =>
          maybeOverlay = None
          dispatchAction(action)
          repaint()
        case Right(overlay) =>
          maybeOverlay = Some(overlay)
          overlay.message.foreach(message => messagePane.addMessage(message))
          repaint()
      }
    }
  }

  private def dispatchAction(action: PlayerAction): Unit = {
    val results: List[ActionResult] = game.takeTurn(action)
    results.foreach(result => applyResult(result))
  }

  private def applyResult(result: ActionResult): Unit = result match {
    case MessageResult(message) => messagePane.addMessage(message)
    case NoResult               =>
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

    val lineHeight = g.getFontMetrics(font).getHeight
    val charWidth = g.getFontMetrics(font).charWidth(' ')

    messagePane.paint(g, 0, 0)
    playerPane.paint(g, 0, lineHeight)
    levelPane.paint(g, (PlayerPane.WidthInChars + 1) * charWidth, lineHeight * 2)

    maybeOverlay match {
      case Some(overlay) =>
        overlay.panel.foreach(p => p.paint(g, font))
      case None =>
    }
  }
}
object GamePanel {
  val WidthInPixels = 1024
  val HeightInPixels = 768
  
  def apply(game: Game, renderer: Renderer): GamePanel = {
    new GamePanel(game, renderer)
  }
}

