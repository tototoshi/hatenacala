package com.tototoshi.hatena

import scala.xml.{Elem, Node}

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

