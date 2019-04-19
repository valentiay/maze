package maze.directed

object unicodeWalls {
  case object Top extends Top("\u255b\u2558")
  case object Right extends Right(" \u2550")
  case object Bottom extends Bottom("\u2555\u2552")
  case object Left extends Left("\u2550 ")
  case object TopRight extends TopRight("\u2575\u2558")
  case object TopBottom extends TopBottom("\u2502\u2502")
  case object TopLeft extends TopLeft("\u255b\u2575")
  case object RightBottom extends RightBottom("\u2577\u2552")
  case object RightLeft extends RightLeft("\u2550\u2550")
  case object BottomLeft extends BottomLeft("\u2555\u2577")
  case object TopRightBottom extends TopRightBottom("\u2502\u255e")
  case object TopRightLeft extends TopRightLeft("\u255b\u2558")
  case object TopBottomLeft extends TopBottomLeft("\u2561\u2502")
  case object RightBottomLeft extends RightBottomLeft("\u2555\u2552")
  case object All extends All("\u2561\u255e")
}
