package config.bindings

import cats.syntax.functor._
import cats.syntax.applicative._
import game.GameController.GameControllerState
import monocle.Lens
import utils.MonadThrow

abstract class MovementBinding[F[_] : MonadThrow](
  bindings: Set[String],
  lens: Lens[GameControllerState, Int],
  f: Int => Int,
) extends ConfiguredBinding[F](bindings) {

  def action(state: GameControllerState): F[GameControllerState] = {
    val newState = lens.modify(f)(state)
    if (state.maze.isInBounds(newState.hero.x, newState.hero.y)) {
      state
        .maze
        .get(newState.hero.x, newState.hero.y)
        .map(cell => if (cell.isWall) state else newState)
    } else {
      state.pure[F]
    }
  }
}