package terminal

import cats.Monad
import cats.effect.Sync
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.traverse._
import cats.instances.list._
import hero.Hero
import maze.Maze
import monocle.macros.Lenses
import terminal.bindings.Binding
import utils.MonadThrow

import scala.io.StdIn

object Controller {
  @Lenses
  case class ControllerState(maze: Maze, hero: Hero, ready: Boolean = false)


  def run[F[_]: MonadThrow: Sync](bindings: List[Binding[F]])(state: ControllerState): F[ControllerState] = {
    val mazePrinter = new MazePrinter[F](state.maze, state.hero)
    for {
      _ <- (for (y <- 0 until state.maze.size._2) yield mazePrinter.printLine(y).map(_ => println())).toList.sequence
      command = StdIn.readLine()
      newState <- Monad[F]
        .tailRecM[(ControllerState, List[Binding[F]]), ControllerState]((state, bindings)) {
          case (newState, newBindings) =>
            newBindings match {
              case head :: tail => head.process(newState, command).map(s => Left((s, tail)))
              case Nil => Monad[F].pure(Right(newState))
            }
      }
    } yield newState
  }
}