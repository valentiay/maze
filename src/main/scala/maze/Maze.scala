package maze

import cats.syntax.applicative._
import cats.syntax.functor._
import cats.syntax.flatMap._
import utils._

trait Maze {
  def size: (Int, Int)

  def get[F[_] : MonadThrow](x: Int, y: Int): F[MazeCell]
}

class MatrixMaze private(matrix: Array[Array[MazeCell]]) extends Maze {
  val size: (Int, Int) = (matrix.length, matrix.head.length)

  def get[F[_] : MonadThrow](x: Int, y: Int): F[MazeCell] =
    for {
      _ <- new IllegalArgumentException("Coordinates out of bounds")
        .raise
        .whenA((0 > x) || (0 > y) || (x >= size._1) || (y >= size._2))
    } yield matrix(x)(y)
}

object MatrixMaze {
  def mkInstance[F[_] : MonadThrow](matrix: Array[Array[MazeCell]]): F[Maze] =
    for {
      _ <- new IllegalArgumentException("Zero width")
        .raise
        .whenA(matrix.length == 0)
      _ <- new IllegalArgumentException("Columns have different height")
        .raise
        .whenA(matrix.exists(column => column.length != matrix.head.length))
    } yield new MatrixMaze(matrix)
}