package scalangband.ui.gamepanel.overlay

import scalangband.ui.keys.KeyHandler

import scala.swing.{Font, Graphics2D}

trait GamePanelOverlay {
  def message: Option[String]
  def keyHandler: KeyHandler
  def paintable: Option[Paintable]  
}

trait Paintable {
  def x: Int
  def y: Int
  def paint(g: Graphics2D, font: Font): Unit
}