package scalangband.bridge.actionresult

/**
 * Results that are displayed in the ui's messaage line.
 */
case class MessageResult(message: String) extends ActionResult

/**
 * Indicates that the action result requires no communication to the user.
 */
object NoResult extends ActionResult

object DeathResult {
  def apply(): ActionResult = {
    MessageResult("You have died.")
  }
}