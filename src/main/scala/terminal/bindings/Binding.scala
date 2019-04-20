package terminal.bindings

import terminal.Controller.ControllerState

trait Binding[F[_]] {
  def process(state: ControllerState, command: String): F[ControllerState]
}
