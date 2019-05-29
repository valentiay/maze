package config

import maze.generators.MazeGenerator
import config.bindings.Binding
import printers.ShadowGenerator

case class Config[F[_]](
  bindings: List[Binding[F]],
  visibilityRadius: Int,
  shadowGenerator: ShadowGenerator,
  mazeGenerator: MazeGenerator[F],
  mazeHeight: Int,
  mazeWidth: Int,
)
