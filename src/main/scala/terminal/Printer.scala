package terminal

trait Printer[F[_]] {
  def height: Int
  def width: Int
  def printLine(y: Int): F[Unit]
}
