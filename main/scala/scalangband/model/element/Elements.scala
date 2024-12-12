package scalangband.model.element

sealed trait Element {
  def message: Option[String] = None
}
object Cold extends Element {
  override def message: Option[String] = Some("You are covered in frost!")
}
object Fire extends Element {
  override def message: Option[String] = Some("You are enveloped in flames!")
}