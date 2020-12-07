/*
 * Copyright 2019 Benoit Louy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

name := "advent-of-code-2020"

ThisBuild / baseVersion := "0.1"

ThisBuild / organization := "com.github.benoitlouy"
ThisBuild / publishGithubUser := "benoitlouy"
ThisBuild / publishFullName := "Benoit Louy"

ThisBuild / crossScalaVersions := Seq("2.13.2")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.0.0-M4",
  "com.lihaoyi" %% "os-lib" % "0.7.1",
  "co.fs2" %% "fs2-core" % "3.0.0-M6",
  "co.fs2" %% "fs2-io" % "3.0.0-M6",
  "org.typelevel" %% "cats-parse" % "0.1.0"
)
