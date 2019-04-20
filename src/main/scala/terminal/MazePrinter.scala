package terminal

import cats.effect.Sync
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.instances.list._
import cats.syntax.flatMap._
import cats.syntax.applicative._
import hero.Hero
import maze.Maze
import utils.MonadThrow

final class MazePrinter[F[_] : MonadThrow : Sync](maze: Maze, hero: Hero) extends Printer[F] {
  def width: Int = maze.size._1

  def height: Int = maze.size._2

  def printLine(y: Int): F[Unit] =
    (for (x <- 0 until width) yield {
      for {
        cell <- maze.get(x, y)
        isHero = hero.x == x && hero.y == y
        isOverriden = isHero
        _ <- Sync[F].delay(print(hero.repr)).whenA(isHero)
        _ <- Sync[F].delay(print(cell.repr)).whenA(!isOverriden)
      } yield ()
    }).toList.sequence.void
}
