package jp.ddo.ttoshi.hatena

import java.io.{File, FileInputStream}
import java.util.{Date, Properties}
import java.text.SimpleDateFormat
import scala.io.Source
import scala.xml.{Elem, Node}
import jp.ddo.ttoshi.wsse._


object Draft {
  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
  val draftDir: String = HatenaProps.draftDir
  val API = new HatenaAPI(user)
  def add(filePath: String) {
    new HatenaHttpClient(user) POST(API draft, new DraftFile(filePath))
  }
  def list(): List[DraftCollectionEntry] = {
    val xml = new HatenaHttpClient(user) GET(API.draft)
    new DraftCollenction(xml).entries
  }
  def save(id: String) {
    val filePath = (new File(HatenaProps.draftDir)).getAbsolutePath + "/" + id
    save(id, new File(filePath))
  }
  def save(id: String, file: File) {
    new HatenaHttpClient(user) POST(API.draft / id, file) match {
      case true  => println("Successfully saved.")
      case false => println("Error")
    }
  }
  def rm(id: String) {
    new HatenaHttpClient(user) DELETE(API.draft / id) match {
      case true  => println("Successfully deleted.")
      case false => println("Error")
    }
  }
  def dateOf(id: String): String = {
    list.find(_.id == id) match {
      case Some(entry) => entry.dateYYYYMMDD
      case None => error("Draft Entry not found.")
    }
  }

}

class DraftCollenction(val xml: Elem) {
  val entries: List[DraftCollectionEntry] = (xml \\ "entry").map(new DraftCollectionEntry(_)).toList
}

class DraftCollectionEntry(val xml: Node) {
  override def toString = "Draft id: " + id + "  title: " + title + "  date: " + dateYYYYMMDD
  val title = xml \ "title" text
  lazy val id = (xml \ "link" \ "@href").text.split("/").last
  lazy val updated = xml \ "updated" text
  lazy val published = xml \ "published" text
  lazy val dateYYYYMMDD: String = published.take("yyyy-MM-dd".length).filterNot(_=='-')
}

class DraftEntry(val xml: Elem) {
  override def toString = "Draft id: " + id + "  title: " + title + "  date: " + dateYYYYMMDD
  lazy val id = (xml \\ "entry" \ "link" \ "@href").text.split("/").last
  lazy val title = xml \\ "entry" \\ "title" text
  lazy val updated = xml \\ "entry" \\ "updated" text
  lazy val published = xml \\ "entry" \\ "published" text
  lazy val dateYYYYMMDD: String = published.take("yyyy-MM-dd".length).filterNot(_=='-')
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
