package scalangband.model.action

/**
 * The result of a player or monster action, to be communicated to the user.
 */
trait ActionResult {
  def success = true
}
