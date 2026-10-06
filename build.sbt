ThisBuild / scalaVersion := "3.9.0"
ThisBuild / organization := "com.example"
ThisBuild / version      := "0.1.0-SNAPSHOT"

ThisBuild / semanticdbEnabled := true // needed by Scalafix semantic rules

val CatsEffectVersion = "3.7.1"
val Fs2Version        = "3.14.0"
val Http4sVersion     = "0.23.38"
val SkunkVersion      = "1.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "typelevel-app",
    fork := true,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect"         % CatsEffectVersion,
      "co.fs2"        %% "fs2-core"            % Fs2Version,
      "co.fs2"        %% "fs2-io"              % Fs2Version,
      // Outdated on purpose: a known-vulnerable version for the Dependabot alerts demo
      "org.postgresql" % "postgresql"          % "42.2.10",
      "org.http4s"    %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"    %% "http4s-dsl"          % Http4sVersion,
      "org.tpolecat"  %% "skunk-core"          % SkunkVersion
    )
  )
