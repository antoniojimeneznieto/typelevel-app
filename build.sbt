ThisBuild / scalaVersion := "3.9.0"
ThisBuild / organization := "com.example"
ThisBuild / version      := "0.1.0-SNAPSHOT"

// Scalafix
ThisBuild / semanticdbEnabled    := true // needed by Scalafix semantic rules
ThisBuild / scalafixDependencies += "org.typelevel" %% "typelevel-scalafix-cats" % "0.6.0" // provides TypelevelMapSequence
ThisBuild / scalafixConfig       := Some(file(".scalafix.conf")) // where rules are configured (the default location)
ThisBuild / scalafixOnCompile    := false // don't rewrite code on every compile: CI runs `scalafixAll --check`
ThisBuild / scalafixCaching      := true  // incremental: skip files unchanged since the last run

// Don't lint generated code
Compile / scalafix / unmanagedSources :=
  (Compile / unmanagedSources).value.filterNot(_.getPath.contains("generated"))

addCommandAlias("fix", "scalafixAll")              // apply rewrites locally
addCommandAlias("fixCheck", "scalafixAll --check") // same check as CI

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
