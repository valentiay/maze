package maze.generators

import cats.Monad
import maze._
import cats.syntax.option._
import cats.syntax.flatMap._
import cats.syntax.functor._
import maze.directed.DirectedWallsFactory
import utils._

import scala.util.Random

object dfs {
  def mkInstance[F[_] : MonadThrow](factory: DirectedWallsFactory): MazeGenerator[F] = (w, h) => {
    val dfsW = w / 2
    val dfsH = h / 2
    val visitedFrom = Array.fill[Option[(Int, Int)]](dfsW, dfsH)(None)
    visitedFrom(0)(0) = (0, 0).some

    def isInBounds(x: Int, y: Int): Boolean =
      0 <= x && 0 <= y && x < dfsW && y < dfsH

    def mkDfsMatrix: F[Unit] = {
      Monad[F].tailRecM[(Int, Int), Unit]((0, 0)) { case (currentX, currentY) =>
        val candidates =
          Seq(
            (currentX - 1, currentY),
            (currentX, currentY - 1),
            (currentX + 1, currentY),
            (currentX, currentY + 1)
          ).filter { case (x, y) => isInBounds(x, y) && visitedFrom(x)(y).isEmpty }

        if (candidates.nonEmpty) {
          val (nextX, nextY) = candidates(Random.nextInt(candidates.size))
          visitedFrom(nextX)(nextY) = (currentX, currentY).some
          Monad[F].pure(Left((nextX, nextY)))
        } else if (currentX == 0 && currentY == 0) {
          Monad[F].pure(Right(()))
        } else {
          visitedFrom(currentX)(currentY)
            .liftTo[F](new IllegalStateException("visitedFrom corrupted"))
            .map(Left.apply)
        }
      }
    }

    def isWall(x: Int, y: Int): Boolean = {
      val dfsX = x / 2
      val dfsY = y / 2

      if (!isInBounds(dfsX, dfsY)) false
      else if (x % 2 == 0 && y % 2 == 0) false
      else if (x % 2 == 0 && y % 2 == 1) !isInBounds(dfsX, dfsY + 1) ||
        (visitedFrom(dfsX)(dfsY) != (dfsX, dfsY + 1).some && visitedFrom(dfsX)(dfsY + 1) != (dfsX, dfsY).some)
      else if (x % 2 == 1 && y % 2 == 0) !isInBounds(dfsX + 1, dfsY) ||
        (visitedFrom(dfsX)(dfsY) != (dfsX + 1, dfsY).some && visitedFrom(dfsX + 1)(dfsY) != (dfsX, dfsY).some)
      else true
    }

    def getCell(x: Int, y: Int): MazeCell = {
      val top = isWall(x - 1, y - 2)
      val right = isWall(x, y - 1)
      val bottom = isWall(x - 1, y)
      val left = isWall(x - 2, y - 1)
      val self = isWall(x - 1, y - 1)
      if (self) mkCell(factory)(top, right, bottom, left) else factory.space
    }

    def mkMaze: F[Maze] = {
      val matrix = Array.ofDim[MazeCell](w, h)
      for {
        x <- 0 until w
        y <- 0 until h
      } yield matrix(x)(y) = getCell(x, y)
      MatrixMaze.mkInstance(matrix)
    }

    for {
      _ <- mkDfsMatrix
      maze <- mkMaze
    } yield maze
  }
}
