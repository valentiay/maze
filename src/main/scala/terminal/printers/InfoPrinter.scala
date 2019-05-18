package terminal.printers

import cats.effect.Sync
import config.Config
import terminal.Controller.ControllerState
import utils.MonadThrow

final class InfoPrinter[F[_] : MonadThrow : Sync](config: Config[F])(state: ControllerState) extends Printer[F]{
  val width: Int = 30
  val height: Int = state.maze.size._2

  private def appendWIthSpaces(string: String): String =
    string + (for (_ <- string.length until width) yield " ").mkString

  val text =
    s"""
      | Movement
      |   t/w - Top, r/d - Right,
      |   b/s - Bottom, l/a - Left
      |
      | Enter letter twice to move
      | up to the wall.
      |
      |
      | Legend
      |   A  - Player
      |   ** - Shadow
    """.stripMargin.split("\n").map(appendWIthSpaces)

  def printLine(y: Int): F[Unit] = if (y < text.length) Sync[F].delay(print(text(y))) else Sync[F].unit
}
