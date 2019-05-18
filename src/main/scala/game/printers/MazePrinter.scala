package game.printers

import cats.effect.Sync
import cats.instances.list._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.traverse._
import config.Config
import game.GameController.GameControllerState
import utils.MonadThrow

import scala.annotation.tailrec

final class MazePrinter[F[_] : MonadThrow : Sync](config: Config[F])(state: GameControllerState) extends Printer[F] {
  def width: Int = state.maze.size._1
  def height: Int = state.maze.size._2

  def printLine(y: Int): F[Unit] =
    (for (x <- 0 until width) yield {
      for {
        cell <- state.maze.get(x, y)
      } yield {
        val printHero: () => Boolean = () => {
          val isHero = state.hero.x == x && state.hero.y == y
          if (isHero) print(state.hero.repr)
          isHero
        }
        val printObjective: () => Boolean = () => {
          val isObjective = x == config.mazeWidth - 2 && y == config.mazeHeight - 2
          if (isObjective) print("}{")
          isObjective
        }
        val printShadow: () => Boolean = () => {
          val isShadow = Math.pow(state.hero.x - x, 2) + Math.pow(state.hero.y - y, 2) > Math.pow(config.visibilityRadius, 2)
          if (isShadow) print(config.shadowGenerator.nextSymbol)
          isShadow
        }

        @tailrec
        def go(handlers: List[() => Boolean]): Boolean =
          handlers match {
            case handler :: tail =>
              if (handler()) {
                true
              } else {
                go(tail)
              }
            case Nil => false
          }

        if (!go(List(printHero, printObjective, printShadow))) {
          print(cell.repr)
        }
      }
    }).toList.sequence.void
}
