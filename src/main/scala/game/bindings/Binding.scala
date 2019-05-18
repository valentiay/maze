package game.bindings

import game.GameController.GameControllerState

trait Binding[F[_]] {
  def process(state: GameControllerState, command: String): F[GameControllerState]
}
