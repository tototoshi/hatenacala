package com.tototoshi.hatena

import scala.xml.Elem

class DraftEntry(val xml: Elem) {
  override def toString = "Draft id: " + id + "  title: " + title + "  date: " + dateYYYYMMDD
  lazy val id = (xml \\ "entry" \ "link" \ "@href").text.split("/").last
  lazy val title = xml \\ "entry" \\ "title" text
  lazy val content = xml \\ "entry" \\ "content" text
  lazy val updated = xml \\ "entry" \\ "updated" text
  lazy val published = xml \\ "entry" \\ "published" text
  lazy val dateYYYYMMDD: String = published.take("yyyy-MM-dd".length).filterNot(_=='-')
  def open() {
    println(xml \\ "entry" \\ "title" text)
    println(xml \\ "content" text)
  }
}

