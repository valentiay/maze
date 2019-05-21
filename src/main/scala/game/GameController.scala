package game

import cats.Monad
import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.syntax.applicative._
import cats.instances.list._
import config.Config
import hero.Hero
import maze.Maze
import monocle.macros.Lenses
import game.bindings.Binding
import game.printers.{InfoPrinter, MazePrinter}
import utils.MonadThrow

import scala.io.StdIn
import scala.sys.process._

object GameController {

  @Lenses
  case class GameControllerState(maze: Maze, hero: Hero, ready: Boolean = false)

  def render[F[_]: Sync](config: Config[F], state: GameControllerState): F[Unit] = {
    val mazePrinter = new MazePrinter[F](config)(state)
    val infoPrinter = new InfoPrinter[F](config)(state)
    (for (y <- 0 until state.maze.size._2) yield {
      mazePrinter.printLine(y) >> infoPrinter.printLine(y) >> Sync[F].delay(println())
    }).toList.sequence.void
  }

  def clear[F[_]: Sync]: F[Unit] = {
    if (System.getProperty("os.name") == "Linux") {
      Sync[F].delay("clear".!)
    } else if (System.getProperty("os.name") == "Windows") {
      Sync[F].delay("cls".!)
    } else {
      ().pure[F]
    }
  }

  def run[F[_] : MonadThrow : Sync](config: Config[F])(state: GameControllerState): F[GameControllerState] = {
    for {
      _ <- clear
      _ <- render(config, state)
      _ <- Sync[F].delay(print("> "))

      command <- Sync[F].delay(StdIn.readLine())
      newState <- Monad[F]
        .tailRecM[(GameControllerState, List[Binding[F]]), GameControllerState]((state, config.bindings)) {
        case (newState, newBindings) =>
          newBindings match {
            case head :: tail => head.process(newState, command).map(s => Left((s, tail)))
            case Nil => Monad[F].pure(Right(newState))
          }
      }
    } yield {
      if (newState.hero.x == config.mazeWidth - 2 && newState.hero.y == config.mazeHeight - 2) {
        newState.copy(ready = true)
      } else {
        newState
      }
    }
  }
}
