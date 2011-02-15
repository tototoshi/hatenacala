import sbt._

class ScalaHatenaProject(info: ProjectInfo) extends DefaultProject(info) with ProguardProject with AssemblyProject {
  lazy val scalatest = "org.scalatest" % "scalatest" % "1.2"
  lazy val httpclient = "org.apache.httpcomponents" % "httpclient" % "4.1"

  override def mainClass = Some("com.tototoshi.hatena.Hatena")

  def distPath = ((outputPath ##) / defaultJarName) +++ mainDependencies.scalaJars

  lazy val dist = zipTask(distPath, "dist", "distribution.zip") dependsOn (`package`) describedAs("Zips up the project.")
}

