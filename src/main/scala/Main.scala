import cats.Monad
import cats.effect.SyncIO
import config.SettingsController
import game.{GameController, Hero, VictoryController}
import game.GameController.GameControllerState

object Main {
  def main(args: Array[String]): Unit = {
    (for {
      config <- SettingsController.run[SyncIO]()
      maze <- config.mazeGenerator.apply(config.mazeWidth, config.mazeHeight)
      _ <- Monad[SyncIO].iterateUntilM(GameControllerState(maze, Hero(1, 1, "A ")))(GameController.run[SyncIO](config))(_.ready)
      _ <- VictoryController.run[SyncIO]()
    } yield ()).unsafeRunSync()
  }
}
