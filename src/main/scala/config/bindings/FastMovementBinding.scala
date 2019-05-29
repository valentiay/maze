package config.bindings

import cats.Monad
import cats.syntax.functor._
import game.GameController.GameControllerState
import monocle.Lens
import utils.MonadThrow

abstract class FastMovementBinding[F[_] : MonadThrow](
  bindings: Set[String],
  lens: Lens[GameControllerState, Int],
  f: Int => Int,
) extends ConfiguredBinding[F](bindings) {

  def action(state: GameControllerState): F[GameControllerState] = {
    Monad[F].tailRecM(state) { state =>
      val newState = lens.modify(f)(state)
      if (state.maze.isInBounds(newState.hero.x, newState.hero.y)) {
        state
          .maze
          .get(newState.hero.x, newState.hero.y)
          .map(cell => if (cell.isWall) Right(state) else Left(newState))
      } else {
        Monad[F].pure(Right(state))
      }
    }
  }
}