import sbt._

class ScalaHatenaProject(info: ProjectInfo) extends DefaultWebProject(info) {
  lazy val scalatest = "org.scalatest" % "scalatest" % "1.2"
  lazy val httpclient = "org.apache.httpcomponents" % "httpclient" % "4.1"
}

