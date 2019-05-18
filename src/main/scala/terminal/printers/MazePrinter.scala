package terminal.printers

import cats.effect.Sync
import cats.instances.list._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.traverse._
import config.Config
import terminal.Controller.ControllerState
import utils.MonadThrow

final class MazePrinter[F[_] : MonadThrow : Sync](config: Config[F])(state: ControllerState) extends Printer[F] {
  def width: Int = state.maze.size._1
  def height: Int = state.maze.size._2

  def printLine(y: Int): F[Unit] =
    (for (x <- 0 until width) yield {
      for {
        cell <- state.maze.get(x, y)
        isHero = state.hero.x == x && state.hero.y == y
        isShadow = Math.pow(state.hero.x - x, 2) + Math.pow(state.hero.y - y, 2) > Math.pow(config.visibilityRadius, 2)
        isOverriden = isHero || isShadow
        _ <- Sync[F].delay(print(state.hero.repr)).whenA(isHero)
        _ <- Sync[F].delay(print(config.shadowGenerator.nextSymbol)).whenA(isShadow)
        _ <- Sync[F].delay(print(cell.repr)).whenA(!isOverriden)
      } yield ()
    }).toList.sequence.void
}
