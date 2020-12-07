package com.benoitlouy.aoc2020

import cats.effect._
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import fs2.io.file.Files
import fs2.text

object Day4 extends IOApp.Simple {

  final case class Acc(count: Int, attr: Set[String])

  val reqFields = Set(
    "byr",
    "iyr",
    "eyr",
    "hgt",
    "hcl",
    "ecl",
    "pid"
  )

  def validate(attrs: Set[String]) = attrs.intersect(reqFields) == reqFields

  override def run: IO[Unit] = {
    val resPath = "/day4/input"
    for {
      nullableResource <- IO(getClass().getResource(resPath))
      path <-
        if (Option(nullableResource).isEmpty)
          IO.raiseError(new NoSuchFileException(resPath))
        else IO.pure(Paths.get(nullableResource.toURI).toAbsolutePath)
      _ <- Files[IO]
        .readAll(path, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .fold(Acc(0, Set.empty)) {
          case (Acc(count, attrs), l) if l.isEmpty && validate(attrs) =>
            Acc(count + 1, Set.empty)
          case (Acc(count, _), l) if l.isEmpty => Acc(count, Set.empty)
          case (Acc(count, attrs), l) =>
            val keys = l.split(' ').map(_.split(':').head).toSet
            Acc(count, attrs ++ keys)
        }
        .last
        .foreach(e => IO(println(e)))
        .compile
        .drain
    } yield ()
  }

}
