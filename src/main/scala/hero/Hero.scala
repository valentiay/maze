package hero

import monocle.macros.Lenses

@Lenses
case class Hero(x: Int, y: Int, repr: String)
