package com.tototoshi.hatena

import java.io.{File, FileInputStream}
import java.util.{Date, Properties}
import java.text.SimpleDateFormat
import scala.io.Source
import scala.xml.{Elem, Node}

object Draft {
  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
  val draftDir: String = HatenaProps.draftDir
  val API = new HatenaAPI(user)

  def add(filePath: String) {
    new HatenaHttpClient(user) POST(API.draft, new DraftFile(filePath))
  }

  def list(): List[DraftCollectionEntry] = {
    val xml = new HatenaHttpClient(user) GET(API.draft)
    new DraftCollenction(xml).entries
  }

  def open(id: String) {
    val entry = new DraftEntry(new HatenaHttpClient(user) GET(API.draft / id))
    println(entry.title)
    println(entry.content)
  }

  def update(id: String, filePath: String) {
    new HatenaHttpClient(user) PUT(API.draft / id, new DraftFile(filePath))
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

