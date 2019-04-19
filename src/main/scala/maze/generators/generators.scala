package maze

import maze.directed.DirectedWallsFactory

package object generators {
  type MazeGenerator[F[_]] = (Int, Int) => F[Maze]

  def mkCell(factory: DirectedWallsFactory)(top: Boolean, right: Boolean, bottom: Boolean, left: Boolean): MazeCell =
    (top, right, bottom, left) match {
      case (false, false, false, false) => factory.space
      case (true, false, false, false)  => factory.top
      case (false, true, false, false)  => factory.right
      case (true, true, false, false)   => factory.topRight
      case (false, false, true, false)  => factory.bottom
      case (true, false, true, false)   => factory.topBottom
      case (false, true, true, false)   => factory.rightBottom
      case (true, true, true, false)    => factory.topRightBottom
      case (false, false, false, true)  => factory.left
      case (true, false, false, true)   => factory.topLeft
      case (false, true, false, true)   => factory.rightLeft
      case (true, true, false, true)    => factory.topRightLeft
      case (false, false, true, true)   => factory.bottomLeft
      case (true, false, true, true)    => factory.topBottomLeft
      case (false, true, true, true)    => factory.rightBottomLeft
      case (true, true, true, true)     => factory.all
    }

}
