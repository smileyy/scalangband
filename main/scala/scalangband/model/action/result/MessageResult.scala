package scalangband.model.action.result

case class MessageResult(messages: List[String]) extends ActionResult
object MessageResult {
  def apply(message: String): MessageResult = MessageResult(List(message))
}