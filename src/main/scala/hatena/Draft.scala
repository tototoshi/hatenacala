package jp.ddo.ttoshi.hatena

import java.io.{File, FileInputStream}
import java.util.Properties

import jp.ddo.ttoshi.wsse._


object Draft {
  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
  val draftDir: String = HatenaProps.draftDir
  val API = new HatenaAPI(user)
  def add(filePath: String) {
    new HatenaHttpClient(user) POST(API.drafts, new File(filePath))
  }
  def list(): List[Draft] = {
    val xml = new HatenaHttpClient(user) GET(API.drafts)
    val entries = xml \\ "entry"
    val links = entries.map(_ \\ "link")
    val ids = links.map(link => (link \ "@href").text.split("/").last)
    ids.map(x => new Draft(user, x)).toList
  }
  def save(id: String) {
    val filePath = (new File(HatenaProps.draftDir)).getAbsolutePath + "/" + id
    save(id, new File(filePath))
  }
  def save(id: String, file: File) {
    new HatenaHttpClient(user) POST(API draft id, file) match {
      case true  => println("SUCCESS")
      case false => println("FALSE")
    }
  }

}

class Draft(val user: HatenaUser, val id: String = "") {
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

