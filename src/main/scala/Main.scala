import cats.Monad
import cats.effect.SyncIO
import cats.syntax.traverse._
import cats.instances.list._
import config.Config
import hero.Hero
import maze.directed.{SimpleWallsFactory, UnicodeWallsFactory}
import maze.generators.dfs
import terminal.Controller.ControllerState
import terminal.bindings.movement._
import terminal.Controller
import terminal.printers.UnicodeShadowGenerator

object Main {
  def main(args: Array[String]): Unit = {
    val config =
      Config(
        bindings = List(
          new TopBinding[SyncIO](Set("t", "w")),
          new RightBinding[SyncIO](Set("r", "d")),
          new BottomBinding[SyncIO](Set("b", "s")),
          new LeftBinding[SyncIO](Set("l", "a")),
          new FastTopBinding[SyncIO](Set("tt", "ww")),
          new FastRightBinding[SyncIO](Set("rr", "dd")),
          new FastBottomBinding[SyncIO](Set("bb", "ss")),
          new FastLeftBinding[SyncIO](Set("ll", "aa")),
        ),
        visibilityRadius = 3,
        shadowGenerator = UnicodeShadowGenerator,
      )
    val mazeGenerator = dfs.mkInstance[SyncIO](UnicodeWallsFactory)
    (for {
      maze <- mazeGenerator.apply(81, 17)
      _ <- Monad[SyncIO].iterateUntilM(ControllerState(maze, Hero(1, 1, "A ")))(Controller.run[SyncIO](config))(_.ready)
    } yield ()).unsafeRunSync()
  }
}
