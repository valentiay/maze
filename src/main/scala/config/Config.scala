package config

import terminal.bindings.Binding
import terminal.printers.ShadowGenerator

case class Config[F[_]](
  bindings: List[Binding[F]],
  visibilityRadius: Int,
  shadowGenerator: ShadowGenerator,
)
