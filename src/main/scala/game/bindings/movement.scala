package game.bindings

import game.GameController.GameControllerState
import hero.Hero
import utils.MonadThrow

object movement {
  final class TopBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, GameControllerState.hero composeLens Hero.y, _ - 1)

  final class RightBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, GameControllerState.hero composeLens Hero.x, _ + 1)

  final class BottomBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, GameControllerState.hero composeLens Hero.y, _ + 1)

  final class LeftBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, GameControllerState.hero composeLens Hero.x, _ - 1)

  final class FastTopBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, GameControllerState.hero composeLens Hero.y, _ - 1)

  final class FastRightBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, GameControllerState.hero composeLens Hero.x, _ + 1)

  final class FastBottomBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, GameControllerState.hero composeLens Hero.y, _ + 1)

  final class FastLeftBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, GameControllerState.hero composeLens Hero.x, _ - 1)
}
