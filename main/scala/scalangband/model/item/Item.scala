package scalangband.model.item

import scalangband.model.Representable

trait Item extends Representable {
  def name: String
  def displayName: String
}
