package scalangband.ui.gamepanel.overlay

import scalangband.ui.keys.KeyHandler

import scala.swing.{Font, Graphics2D}

trait GamePanelOverlay {
  def message: Option[String]
  def keyHandler: KeyHandler
  def panel: Option[OverlayPane]  
}

trait OverlayPane {
  def paint(g: Graphics2D, font: Font): Unit
}