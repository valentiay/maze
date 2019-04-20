package terminal.bindings

import cats.Applicative
import cats.syntax.applicative._
import terminal.Controller.ControllerState

abstract class ConfiguredBinding[F[_]: Applicative](bindings: Set[String]) extends Binding[F] {
  def action(controllerState: ControllerState): F[ControllerState]

  def process(state: ControllerState, command: String): F[ControllerState] =
    if (bindings.contains(command)) action(state) else state.pure[F]
}
