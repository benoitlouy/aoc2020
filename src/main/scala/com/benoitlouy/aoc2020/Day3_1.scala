package com.benoitlouy.aoc2020

import cats.effect._
import cats.implicits._
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import fs2.io.file.Files
import fs2.text

object Day3_1 extends IOApp.Simple {

  case class Acc(first: Boolean, pos: Int, count: Int, step: Int)

  val slopes = Vector(
    Vector(1),
    Vector(3),
    Vector(5),
    Vector(7),
    Vector(1, 0)
  )

  override def run: IO[Unit] = {

    def compute(slope: Vector[Int]) = {
      val resPath = "/day3/input"
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
          .fold(Acc(true, 0, 0, 0)) {
            case (Acc(first, pos, count, step), line) =>
              val newCount =
                if (step != 0 || first || line(pos) != '#') count
                else count + 1
              val newPos = (pos + slope(step)) % line.length
              val newStep = (step + 1) % slope.length
              Acc(false, newPos, newCount, newStep)
          }
          .map(_.count)
          .compile
          .last
      } yield res
    }

    slopes.traverse(compute).map(_.flatten.reduce(_ * _)).map(println)
  }
}
