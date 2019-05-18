package game.printers

trait ShadowGenerator {
  def nextSymbol: String
}

object AsciiShadowGenerator extends ShadowGenerator {
  def nextSymbol: String = "**"
}