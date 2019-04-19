import cats.MonadError

package object utils {
  type MonadThrow[F[_]] = MonadError[F, Throwable]

  implicit class RaiseOps(err: Throwable) {
    def raise[F[_] : MonadThrow]: F[Unit] = MonadError[F, Throwable].raiseError(err)
  }
}
