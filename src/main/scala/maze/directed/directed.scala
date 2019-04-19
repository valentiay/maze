package maze

import maze.Wall

package object directed {
  abstract class Top(repr: String) extends Wall(repr)
  abstract class Right(repr: String) extends Wall(repr)
  abstract class Bottom(repr: String) extends Wall(repr)
  abstract class Left(repr: String) extends Wall(repr)
  abstract class TopRight(repr: String) extends Wall(repr)
  abstract class TopBottom(repr: String) extends Wall(repr)
  abstract class TopLeft(repr: String) extends Wall(repr)
  abstract class RightBottom(repr: String) extends Wall(repr)
  abstract class RightLeft(repr: String) extends Wall(repr)
  abstract class BottomLeft(repr: String) extends Wall(repr)
  abstract class TopRightBottom(repr: String) extends Wall(repr)
  abstract class TopRightLeft(repr: String) extends Wall(repr)
  abstract class TopBottomLeft(repr: String) extends Wall(repr)
  abstract class RightBottomLeft(repr: String) extends Wall(repr)
  abstract class All(repr: String) extends Wall(repr)
}
