import sbt._
import Keys._

object MyProject extends Build {

  lazy val root = Project (
    id = "hatenacala",
    base = file ("."),
    settings = Defaults.defaultSettings ++ Seq (
      name := "hateda"
      , version := "0.1-SNAPSHOT"
      , organization := "com.github.tototoshi"
      , libraryDependencies ++= Seq(
        "org.scalaz" %% "scalaz-core" % "6.0.4"
        , "org.scalatest" %% "scalatest" % "1.7.1" % "test"
        , "org.apache.httpcomponents" % "httpclient" % "4.1"
        , "com.typesafe" % "config" % "0.4.0"
        , "org.scala-sbt" %% "launcher-interface" % "0.11.3" % "provided"
      )
    )
  )
}


