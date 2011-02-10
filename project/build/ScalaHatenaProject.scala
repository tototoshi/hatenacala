import sbt._

class ScalaHatenaProject(info: ProjectInfo) extends DefaultProject(info) with ProguardProject with AssemblyProject {
  lazy val scalatest = "org.scalatest" % "scalatest" % "1.2"
  lazy val httpclient = "org.apache.httpcomponents" % "httpclient" % "4.1"

  override def mainClass = Some("com.tototoshi.hatena.Hatena")

  def keepMainClass = """
  -keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
  }
  """
  override def proguardDefaultArgs = "-dontwarn" :: "-dontoptimize" :: "-dontobfuscate" :: keepMainClass :: proguardOptions

}

