package jp.ddo.ttoshi.hatena

import java.io.{File, FileInputStream}
import java.util.{Date, Properties}
import java.text.SimpleDateFormat
import scala.io.Source
import jp.ddo.ttoshi.wsse._


object Draft {
  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
  val draftDir: String = HatenaProps.draftDir
  val API = new HatenaAPI(user)
  def add(filePath: String) {
    new HatenaHttpClient(user) POST(API.drafts, new DraftFile(filePath))
  }
  def list(): List[DraftEntry] = {
    val xml = new HatenaHttpClient(user) GET(API.drafts)
    val entries = xml \\ "entry"
    val links = entries.map(_ \\ "link")
    val ids = links.map(link => (link \ "@href").text.split("/").last)
    ids.map(x => new DraftEntry(user, x)).toList
  }
  def save(id: String) {
    val filePath = (new File(HatenaProps.draftDir)).getAbsolutePath + "/" + id
    save(id, new File(filePath))
  }
  def save(id: String, file: File) {
    new HatenaHttpClient(user) POST(API draft id, file) match {
      case true  => println("Successfully saved.")
      case false => println("Error")
    }
  }
}

class DraftEntry(val user: HatenaUser, val id: String = "") {
  val API = new HatenaAPI(user)
  override def toString = "Draft: id = " + id + "  title: " + title + "  updated: " + updated
  lazy val xml = new HatenaHttpClient(user) GET(API draft id)
  lazy val title = xml \\ "entry" \\ "title" text
  lazy val updated = xml \\ "entry" \\ "updated" text
  def open() {
    println(xml \\ "entry" \\ "title" text)
    println(xml \\ "content" text)
  }
}

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
