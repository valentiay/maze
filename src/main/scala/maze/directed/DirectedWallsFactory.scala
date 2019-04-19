package maze.directed

import maze.Space

trait DirectedWallsFactory {
  def space: Space
  def top: Top
  def right: Right
  def bottom: Bottom
  def left: Left
  def topRight: TopRight
  def topBottom: TopBottom
  def topLeft: TopLeft
  def rightBottom: RightBottom
  def rightLeft: RightLeft
  def bottomLeft: BottomLeft
  def topRightBottom: TopRightBottom
  def topRightLeft: TopRightLeft
  def topBottomLeft: TopBottomLeft
  def rightBottomLeft: RightBottomLeft
  def all: All
}
