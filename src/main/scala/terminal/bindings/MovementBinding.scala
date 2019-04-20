package terminal.bindings

import cats.syntax.functor._
import cats.syntax.applicative._
import monocle.Lens
import terminal.Controller.ControllerState
import utils.MonadThrow

abstract class MovementBinding[F[_] : MonadThrow](
  bindings: Set[String],
  lens: Lens[ControllerState, Int],
  f: Int => Int,
) extends ConfiguredBinding[F](bindings) {

  def action(state: ControllerState): F[ControllerState] = {
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