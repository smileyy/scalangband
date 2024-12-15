package scalangband.ui.gamepanel.overlay

import scalangband.ui.keys.{DirectionKeyHandler, KeyHandler, OpenActionFactory}

object OpenOverlay extends GamePanelOverlay {
  override def message: Option[String] = Some("Choose a direction...")
  override def keyHandler: KeyHandler = DirectionKeyHandler(OpenActionFactory)
  override def paintable: Option[OverlayPanel] = None
}
