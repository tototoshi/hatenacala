package com.tototoshi.hatena

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import scala.io.Source

class DraftFile(val file: File) {
  def this(path: String) = this(new File(path))
  private val src = Source.fromFile(file)
  val (title, content) = src.getLines.toList match {
    case List() => ("", "")
    case xs => (xs.take(1), xs.drop(1).mkString("\n"))
  }
  src.close()
  lazy val created: String = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date)
  def toXML() =
    <entry xmlns="http://purl.org/atom/ns#">
      <title>{title}</title>
      <content type="text/plain">{content}</content>
    <updated>{created}</updated>
    </entry>
}
