package printers

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import config.Config
import game.GameController.GameControllerState
import utils.MonadThrow

final class InfoPrinter[F[_] : MonadThrow : Sync](config: Config[F])(state: GameControllerState) extends Printer[F]{
  val width: Int = 30
  val height: Int = state.maze.size._2

  private def appendWIthSpaces(string: String): String =
    string + (for (_ <- string.length until width) yield " ").mkString

  val text: F[Array[String]] =
    state.maze.get(0 ,0).map(wall =>
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
      |   ${wall.repr} - Wall
      |   ${config.shadowGenerator.nextSymbol} - Shadow
      |   }{ - Objective
      |
    """.stripMargin.split("\n").map(appendWIthSpaces))

  def printLine(y: Int): F[Unit] = text.flatMap(text =>
    if (y < text.length) {
      Sync[F].delay(print(text(y)))
    } else {
      Sync[F].unit
    })
}
