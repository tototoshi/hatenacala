import sbt._
import Keys._

object MyProject extends Build {

  lazy val root = Project (
    id = "hatenacala",
    base = file ("."),
    settings = Defaults.defaultSettings ++ Seq (
      libraryDependencies ++= Seq(
        "org.scalaz" %% "scalaz-core" % "6.0.4",
        "org.scalatest" %% "scalatest" % "1.7.1" % "test",
        "org.apache.httpcomponents" % "httpclient" % "4.1"
      )
    )
  )
}


