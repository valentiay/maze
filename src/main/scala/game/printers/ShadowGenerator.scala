package game.printers

import scala.util.Random

trait ShadowGenerator {
  def nextSymbol: String
}

object AsciiShadowGenerator extends ShadowGenerator {
  def nextSymbol: String = "**"
}