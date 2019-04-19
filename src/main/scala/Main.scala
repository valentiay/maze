import cats.effect.SyncIO
import cats.syntax.traverse._
import cats.instances.list._
import maze.directed.UnicodeWallsFactory
import maze.generators.DfsMazeGenerator

object Main {
  def main(args: Array[String]): Unit = {
    val mazeGenerator = DfsMazeGenerator.mkInstance[SyncIO](UnicodeWallsFactory)
    mazeGenerator(41, 13).flatMap { maze =>
      val (w, h) = maze.size
      (for {
        y <- 0 until h
        x <- 0 until w
      } yield {
        maze.get[SyncIO](x, y).map(c => print(c.repr)).map(_ => if (x == w - 1) println())
      }).toList.sequence
    }.unsafeRunSync()
  }
}
