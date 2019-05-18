package game

import cats.effect.Sync

object VictoryController {
  def run[F[_]: Sync](): F[Unit] = {
    Sync[F].delay(println("Congratulations, you won! Exiting..."))
  }
}
