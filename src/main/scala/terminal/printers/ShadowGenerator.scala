package terminal.printers

import scala.util.Random

trait ShadowGenerator {
  def nextSymbol: String
}

object UnicodeShadowGenerator extends ShadowGenerator {
  def nextSymbol: String = "**"
}