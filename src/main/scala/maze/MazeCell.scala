package maze

trait MazeCell {
  def repr: String
  def isWall: Boolean
}

trait Wall extends MazeCell {
  val isWall = true
}

abstract class WallRepr(val repr: String) extends Wall

trait Space extends MazeCell {
  val isWall = false
}

abstract class SpaceRepr(val repr: String) extends Space

case object SimpleSpace extends SpaceRepr("  ")