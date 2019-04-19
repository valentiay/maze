package maze.generators

import maze._
import cats.syntax.option._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import maze.directed.DirectedWallsFactory
import utils._

import scala.util.Random

object DfsMazeGenerator {
  def mkInstance[F[_] : MonadThrow](factory: DirectedWallsFactory): MazeGenerator[F] = (w, h) => {
    Random.setSeed(42)
    val dfsW = w / 2
    val dfsH = h / 2

    val visitedFrom = Array.ofDim[Option[(Int, Int)]](dfsW, dfsH)

    def isInBounds(x: Int, y: Int): Boolean =
      0 <= x && 0 <= y && x < dfsW && y < dfsH

    for (x <- 0 until dfsW; y <- 0 until dfsH) visitedFrom(x)(y) = None

    visitedFrom(0)(0) = (0, 0).some
    var currentX = 0
    var currentY = 0

    var continue = true
    while (continue) {
      val candidates =
        Seq((currentX - 1, currentY), (currentX, currentY - 1), (currentX + 1, currentY), (currentX, currentY + 1))
          .filter { case (x, y) => isInBounds(x, y) && visitedFrom(x)(y).isEmpty }
      if (candidates.nonEmpty) {
        val (nextX, nextY) = candidates(Random.nextInt(candidates.size))
        visitedFrom(nextX)(nextY) = (currentX, currentY).some
        currentX = nextX
        currentY = nextY
      } else if (currentX == 0 && currentY == 0) {
        continue = false
      } else {
        val (fromX, fromY) = visitedFrom(currentX)(currentY).get
        currentX = fromX
        currentY = fromY
      }

    }

    def hasWall(x1: Int, y1: Int, x2: Int, y2: Int): F[Boolean] = {
      if (isInBounds(x1, y1) && isInBounds(x2, y2)) {
        for {
          _ <- new IllegalArgumentException("Points are either too far or the same")
            .raise[F]
            .whenA(Math.abs(x1 - x2) + Math.abs(y1 - y2) != 1)
          visitedFrom1 <- visitedFrom(x1)(y1).liftTo[F](new IllegalStateException("visitedFromMatrix is not ready"))
          visitedFrom2 <- visitedFrom(x2)(y2).liftTo[F](new IllegalStateException("visitedFromMatrix is not ready"))
        } yield visitedFrom2 == (x1, y1) || visitedFrom1 == (x2, y2)
      } else {
        false.pure[F]
      }
    }

    def getCell(x: Int, y: Int): F[MazeCell] =
      for {
        top    <- hasWall((x - 1) / 2, (y - 1) / 2, (x - 1) / 2, (y - 1) / 2 - 1)
        right  <- hasWall((x - 1) / 2, (y - 1) / 2, (x - 1) / 2 + 1, (y - 1) / 2)
        bottom <- hasWall((x - 1) / 2, (y - 1) / 2, (x - 1) / 2, (y - 1) / 2 + 1)
        left   <- hasWall((x - 1) / 2, (y - 1) / 2, (x - 1) / 2 - 1, (y - 1) / 2)
      } yield mkCell(factory)(top, right, bottom, left)

    val matrix = Array.ofDim[MazeCell](w, h)
    for (x <- 0 until w) matrix(x)(0) = SolidWall
    for (y <- 0 until h) matrix(0)(y) = SolidWall
    for {
      x <- 0 until dfsW
      y <- 0 until dfsH
    } {
      matrix(2 * x + 1)(2 * y + 1) = SimpleSpace
      matrix(2 * x + 2)(2 * y + 1) = if (x + 1 < dfsW && visitedFrom(x + 1)(y).get == (x, y) || visitedFrom(x)(y).get == (x + 1, y)) {
        SimpleSpace
      } else {
        SolidWall
      }
      matrix(2 * x + 1)(2 * y + 2) = if (y + 1 < dfsH && visitedFrom(x)(y + 1).get == (x, y) || visitedFrom(x)(y).get == (x, y + 1)) {
        SimpleSpace
      } else {
        SolidWall
      }
      matrix(2 * x + 2)(2 * y + 2) = SolidWall
    }

    MatrixMaze.mkInstance(matrix)
  }

}
