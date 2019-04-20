package maze.generators

import maze._
import cats.syntax.option._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.instances.list._
import maze.directed.DirectedWallsFactory
import utils._

import scala.util.Random

object DfsMazeGenerator {
  def mkInstance[F[_] : MonadThrow](factory: DirectedWallsFactory): MazeGenerator[F] = (w, h) => {
//    Random.setSeed(42)
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

    val matrix = Array.ofDim[MazeCell](w, h)
    for {
      x <- 0 until w
      y <- 0 until h
    } yield matrix(x)(y) = getCell(x, y)
    MatrixMaze.mkInstance(matrix)
  }
}
