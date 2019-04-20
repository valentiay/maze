package maze.directed

import maze.{SimpleSpace, Space, WallRepr}

object SimpleWallsFactory extends DirectedWallsFactory {
  def space: Space = SimpleSpace
  def top: Top = UniversalWall
  def right: Right = UniversalWall
  def bottom: Bottom = UniversalWall
  def left: Left = UniversalWall
  def topRight: TopRight = UniversalWall
  def topBottom: TopBottom = UniversalWall
  def topLeft: TopLeft = UniversalWall
  def rightBottom: RightBottom = UniversalWall
  def rightLeft: RightLeft = UniversalWall
  def bottomLeft: BottomLeft = UniversalWall
  def topRightBottom: TopRightBottom = UniversalWall
  def topRightLeft: TopRightLeft = UniversalWall
  def topBottomLeft: TopBottomLeft = UniversalWall
  def rightBottomLeft: RightBottomLeft = UniversalWall
  def all: All = UniversalWall

  case object UniversalWall extends WallRepr("||") with Top with Right with Bottom with Left 
    with TopRight with TopBottom with TopLeft with RightBottom with RightLeft with BottomLeft 
    with TopRightBottom with TopRightLeft with TopBottomLeft with RightBottomLeft with All
}