package scalangband.model.action.result

case class MessageResult(messages: Seq[String]) extends ActionResult
object MessageResult {
  def apply(message: String): MessageResult = MessageResult(Seq(message))
}