package com.benoitlouy.aoc2020

import cats.effect._
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import fs2.io.file.Files
import fs2.text

object Day1_2 extends IOApp.Simple {
  override def run: IO[Unit] = {
    val resPath = "/day1/input"
    for {
      nullableResource <- IO(getClass.getResource(resPath))
      path <-
        if (Option(nullableResource).isEmpty)
          IO.raiseError(new NoSuchFileException(resPath))
        else IO.pure(Paths.get(nullableResource.toURI).toAbsolutePath)
      content = Files[IO]
        .readAll(path, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .filter(s => !s.trim.isEmpty)
        .map(_.toInt)
      permutations = for {
        a <- content
        b <- content
        c <- content
      } yield (a, b, c)
      res = permutations.collectFirst {
        case (a, b, c) if a + b + c == 2020 => a * b * c
      }
      _ <- res.foreach(e => IO(println(e))).compile.drain
    } yield ()
  }
}
