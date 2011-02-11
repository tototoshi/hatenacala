/*
Copyright (c) 2011 Toshiyuki Takahashi.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in
    the documentation and/or other materials provided with the
    distribution.
 3. The name of the author may not be used to endorse or promote
    products derived from this software without specific prior
    written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS
 OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


package com.tototoshi.hatena

import java.io.{File, FileInputStream}
import java.util.{Date, Properties}
import java.text.SimpleDateFormat
import scala.io.Source
import scala.xml.{Elem, Node}

object Draft {
  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
  val API = new HatenaAPI(user)

  def add(filePath: String) {
    new HatenaHttpClient(user) POST(API.draft, new DraftFile(filePath))
  }

  def list(): List[DraftCollectionEntry] = {
    val xml = new HatenaHttpClient(user) GET(API.draft)
    new DraftCollenction(xml).entries
  }

  def get(id: String) {
    val entry = new DraftEntry(new HatenaHttpClient(user) GET(API.draft / id))
    println(entry.title)
    println(entry.content)
  }

  def update(id: String, filePath: String) {
    new HatenaHttpClient(user) PUT(API.draft / id, new DraftFile(filePath))
  }

  def rm(id: String) {
    new HatenaHttpClient(user) DELETE(API.draft / id) match {
      case true  =>
      case false => println("Something is wrong. Failed to remove draft: " + id + ".")
    }
  }

  def dateOf(id: String): String = {
    list.find(_.id == id) match {
      case Some(entry) => entry.dateYYYYMMDD
      case None => error("Draft: " + id + " not found.")
    }
  }

}

