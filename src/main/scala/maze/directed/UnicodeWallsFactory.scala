package maze.directed
import maze.directed.unicodeWalls._
import maze.{SimpleSpace, Space}

object UnicodeWallsFactory extends DirectedWallsFactory {
  def space: Space = SimpleSpace
  def top: Top = Top
  def right: Right = Right
  def bottom: Bottom = Bottom
  def left: Left = Left
  def topRight: TopRight = TopRight
  def topBottom: TopBottom = TopBottom
  def topLeft: TopLeft = TopLeft
  def rightBottom: RightBottom = RightBottom
  def rightLeft: RightLeft = RightLeft
  def bottomLeft: BottomLeft = BottomLeft
  def topRightBottom: TopRightBottom = TopRightBottom
  def topRightLeft: TopRightLeft = TopRightLeft
  def topBottomLeft: TopBottomLeft = TopBottomLeft
  def rightBottomLeft: RightBottomLeft = RightBottomLeft
  def all: All = All
}
