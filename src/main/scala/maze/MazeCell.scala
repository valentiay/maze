package maze

trait MazeCell {
  def repr: String
  def isWall: Boolean
}

abstract class Wall(val repr: String) extends MazeCell {
  val isWall = true
}

case object SolidWall extends Wall("||")


abstract class Space(val repr: String) extends MazeCell {
  val isWall = false
}

case object SimpleSpace extends Space("  ")

case class CustomCell(repr: String, isWall: Boolean) extends MazeCell