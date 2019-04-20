package terminal.bindings

import hero.Hero
import terminal.Controller.ControllerState
import utils.MonadThrow

object movement {
  final class UpBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, ControllerState.hero composeLens Hero.y, _ - 1)

  final class RightBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, ControllerState.hero composeLens Hero.x, _ + 1)

  final class BottomBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, ControllerState.hero composeLens Hero.y, _ + 1)

  final class LeftBinding[F[_] : MonadThrow](bindings: Set[String])
    extends MovementBinding(bindings, ControllerState.hero composeLens Hero.x, _ - 1)

  final class FastUpBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, ControllerState.hero composeLens Hero.y, _ - 1)

  final class FastRightBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, ControllerState.hero composeLens Hero.x, _ + 1)

  final class FastBottomBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, ControllerState.hero composeLens Hero.y, _ + 1)

  final class FastLeftBinding[F[_] : MonadThrow](bindings: Set[String])
    extends FastMovementBinding(bindings, ControllerState.hero composeLens Hero.x, _ - 1)
}
