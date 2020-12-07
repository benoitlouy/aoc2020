package com.benoitlouy.aoc2020

import cats.effect._
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import fs2.io.file.Files
import fs2.text

object Day2_1 extends IOApp.Simple {
  override def run: IO[Unit] = {
    val resPath = "/day2/input"
    for {
      nullableResource <- IO(getClass().getResource(resPath))
      path <-
        if (Option(nullableResource).isEmpty)
          IO.raiseError(new NoSuchFileException(resPath))
        else IO.pure(Paths.get(nullableResource.toURI).toAbsolutePath)
      res <- Files[IO]
        .readAll(path, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .filter(s => !s.trim.isEmpty)
        .map(str => Line.parseLine.parseAll(str))
        .fold(0) {
          case (acc, Right(l)) if l.valid2 => acc + 1
          case (acc, _)                    => acc
        }
        .compile
        .last
      _ <- IO(println(res))
    } yield ()
  }
}

case class Line(min: Int, max: Int, char: Char, password: String) {
  def valid: Boolean =
    Range.inclusive(min, max).contains(password.count(_ == char))
  def valid2: Boolean =
    (password(min - 1) == char && password(
      max - 1
    ) != char) || (password(min - 1) != char && password(max - 1) == char)
}

object Line {
  import cats.parse.{Parser => P, Parser1, Numbers}

  val parser: Parser1[Line] = {
    val int = Numbers.nonNegativeIntString.map(_.toInt)
    val range = ((int <* P.char('-')) ~ int).map { case (min, max) =>
      min -> max
    }
    val str = P.charsWhile(_ => true)
    val line =
      ((range <* P.char(' ')) ~ (P.anyChar <* P.string1(": ")) ~ str)
        .map { case (((min, max), char), password) =>
          Line(min, max, char, password)
        }
    line
  }

  val parseLine = parser <* P.end
}
