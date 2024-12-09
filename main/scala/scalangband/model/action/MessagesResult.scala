package scalangband.model.action

/**
 * Results that are displayed in the ui's messaage line.
 */
case class MessagesResult(messages: List[String], override val success: Boolean = true) extends ActionResult
