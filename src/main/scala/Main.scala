import cats.Monad
import cats.effect.SyncIO
import cats.syntax.traverse._
import cats.instances.list._
import hero.Hero
import maze.directed.{SimpleWallsFactory, UnicodeWallsFactory}
import maze.generators.dfs
import terminal.Controller.ControllerState
import terminal.bindings.movement._
import terminal.{Controller, MazePrinter}

object Main {
  def main(args: Array[String]): Unit = {
    val bindings =
      List(
        new UpBinding[SyncIO](Set("u", "w")),
        new RightBinding[SyncIO](Set("r", "d")),
        new BottomBinding[SyncIO](Set("b", "s")),
        new LeftBinding[SyncIO](Set("l", "a")),
        new FastUpBinding[SyncIO](Set("uu", "ww")),
        new FastRightBinding[SyncIO](Set("rr", "dd")),
        new FastBottomBinding[SyncIO](Set("bb", "ss")),
        new FastLeftBinding[SyncIO](Set("ll", "aa")),
      )

    val mazeGenerator = dfs.mkInstance[SyncIO](UnicodeWallsFactory)
    (for {
      maze <- mazeGenerator.apply(81, 17)
      _ <- Monad[SyncIO].iterateUntilM(ControllerState(maze, Hero(1, 1, "A ")))(Controller.run[SyncIO](bindings))(_.ready)
    } yield ()).unsafeRunSync()
  }
}
