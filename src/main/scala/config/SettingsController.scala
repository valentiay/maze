package config

import java.util.InputMismatchException

import scala.io.StdIn
import cats.ApplicativeError
import cats.effect.Sync
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.applicativeError._
import maze.directed.{AsciiWallsFactory, UnicodeWallsFactory}
import maze.generators.dfs
import config.bindings.movement.{BottomBinding, FastBottomBinding, FastLeftBinding, FastRightBinding, FastTopBinding, LeftBinding, RightBinding, TopBinding}
import printers.AsciiShadowGenerator
import utils._

object SettingsController {
  def bindings[F[_] : MonadThrow] = List(
    new TopBinding[F](Set("t", "w")),
    new RightBinding[F](Set("r", "d")),
    new BottomBinding[F](Set("b", "s")),
    new LeftBinding[F](Set("l", "a")),
    new FastTopBinding[F](Set("tt", "ww")),
    new FastRightBinding[F](Set("rr", "dd")),
    new FastBottomBinding[F](Set("bb", "ss")),
    new FastLeftBinding[F](Set("ll", "aa")),
  )

  sealed trait Mode

  case object Unicode extends Mode

  case object ASCII extends Mode

  /*_*/
  def readMode[F[_] : Sync : MonadThrow]: F[Mode] =
    for {
      _ <- Sync[F].delay(print(
        """
          |Select mode:
          | - (u)nicode for regular characters
          | - (a)scii for ASCII-only characters
          |> """.stripMargin))
      raw <- Sync[F].delay(StdIn.readLine())
      mode <- raw match {
        case "u" | "unicode" => (Unicode: Mode).pure[F]
        case "a" | "ascii" => (ASCII: Mode).pure[F]
        case _ => ApplicativeError[F, Throwable].raiseError(new InputMismatchException("Invalid mode"))
      }
    } yield mode

  def readWidth[F[_] : Sync : MonadThrow]: F[Int] =
    for {
      _ <- Sync[F].delay(print(
        """
          |Enter terminal width in characters (120 is recommended, 40 is minimal)
          |> """
      .stripMargin))
      raw <- Sync[F].delay(StdIn.readInt())
      width <- if (raw >= 40) {
        raw.pure
      } else {
        ApplicativeError[F, Throwable].raiseError(new InputMismatchException("Width too small"))
      }
    } yield width

  def readRadius[F[_] : Sync : MonadThrow]: F[Int] = {
    for {
      _ <- Sync[F].delay(print(
        """
          |Enter the radius of circle player is able to see (3 for hard, 5 for medium, > 7 for easy)
          |> """.stripMargin))
      raw <- Sync[F].delay(StdIn.readInt())
      radius <- if (raw >= 3) {
        raw.pure
      } else {
        ApplicativeError[F, Throwable].raiseError(new InputMismatchException("Radius too small"))
      }
    } yield radius
  }

  /*_*/

  def run[F[_] : Sync : MonadThrow](): F[Config[F]] = {
    (for {
      _ <- Sync[F].delay(print(
        """
          |Welcome to the MAZE game. The goal of the game is reaching the target in the dark labyrinth.
          |Player is able to see some area around himself. Good luck.
        """.stripMargin))
      mode <- readMode[F]
      width <- readWidth[F]
      radius <- readRadius[F]
    } yield {
      val (mazeGen, shadowGen) = mode match {
        case Unicode => (dfs.mkInstance[F](UnicodeWallsFactory), AsciiShadowGenerator)
        case ASCII => (dfs.mkInstance[F](AsciiWallsFactory), AsciiShadowGenerator)
      }

      val mazeWidth = if ((width / 2) % 2 == 0) {
        width / 2 - 1
      } else {
        width / 2
      }

      Config(
        bindings = bindings[F],
        visibilityRadius = radius,
        shadowGenerator = shadowGen,
        mazeGenerator = mazeGen,
        mazeHeight = 21,
        mazeWidth = mazeWidth,
      )
    }).onError { case err: Throwable =>
      println(s"Error occured: ${err.getMessage}. Exiting...")
      System.exit(1).pure[F]
    }
  }
}
