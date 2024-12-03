package scalangband.model.action.result

case class MessagesResult(messages: List[String], override val success: Boolean = true) extends ActionResult
