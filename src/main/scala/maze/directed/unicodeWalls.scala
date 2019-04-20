package maze.directed

import maze.WallRepr

object unicodeWalls {
  case object Top extends WallRepr("\u2551 ") with Top
  case object Right extends WallRepr(" \u2550") with Right
  case object Bottom extends WallRepr("\u2551 ") with Bottom
  case object Left extends WallRepr("\u2550 ") with Left
  case object TopRight extends WallRepr("\u255a\u2550") with TopRight
  case object TopBottom extends WallRepr("\u2551 ") with TopBottom
  case object TopLeft extends WallRepr("\u255d ") with TopLeft
  case object RightBottom extends WallRepr("\u2554\u2550") with RightBottom
  case object RightLeft extends WallRepr("\u2550\u2550") with RightLeft
  case object BottomLeft extends WallRepr("\u2557 ") with BottomLeft
  case object TopRightBottom extends WallRepr("\u2560\u2550") with TopRightBottom
  case object TopRightLeft extends WallRepr("\u2569\u2550") with TopRightLeft
  case object TopBottomLeft extends WallRepr("\u2563 ") with TopBottomLeft
  case object RightBottomLeft extends WallRepr("\u2566\u2550") with RightBottomLeft
  case object All extends WallRepr("\u256c\u2550") with All
}
