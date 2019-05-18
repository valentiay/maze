package game.bindings

import cats.Applicative
import cats.syntax.applicative._
import game.GameController.GameControllerState

abstract class ConfiguredBinding[F[_]: Applicative](bindings: Set[String]) extends Binding[F] {
  def action(controllerState: GameControllerState): F[GameControllerState]

  def process(state: GameControllerState, command: String): F[GameControllerState] =
    if (bindings.contains(command)) action(state) else state.pure[F]
}
