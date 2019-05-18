package terminal

import cats.Monad
import cats.effect.Sync
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.traverse._
import cats.instances.list._
import config.Config
import hero.Hero
import maze.Maze
import monocle.macros.Lenses
import terminal.bindings.Binding
import terminal.printers.{InfoPrinter, MazePrinter}
import utils.MonadThrow

import scala.io.StdIn

object Controller {
  @Lenses
  case class ControllerState(maze: Maze, hero: Hero, ready: Boolean = false)


  def run[F[_]: MonadThrow: Sync](config: Config[F])(state: ControllerState): F[ControllerState] = {
    val mazePrinter = new MazePrinter[F](config)(state)
    val infoPrinter = new InfoPrinter[F](config)(state)
    for {
      _ <- (for (y <- 0 until state.maze.size._2) yield {
        for {
          _ <- mazePrinter.printLine(y)
          _ <- infoPrinter.printLine(y)
        } yield println()
      }).toList.sequence
      command = StdIn.readLine()
      newState <- Monad[F]
        .tailRecM[(ControllerState, List[Binding[F]]), ControllerState]((state, config.bindings)) {
          case (newState, newBindings) =>
            newBindings match {
              case head :: tail => head.process(newState, command).map(s => Left((s, tail)))
              case Nil => Monad[F].pure(Right(newState))
            }
      }
    } yield newState
  }
}