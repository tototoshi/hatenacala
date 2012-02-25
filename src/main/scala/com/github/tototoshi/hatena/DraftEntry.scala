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

package com.github.tototoshi.hatena

import scala.xml.Elem

class DraftEntry(val xml: Elem) extends StringUtils {
  final val PUBLISHED_TIME_FORMAT = "yyyy-MM-dd"

  override def toString = "Draft id: " + id + "  title: " + title + "  date: " + dateYYYYMMDD

  private val entry = xml \\ "entry"
  lazy val id = (entry \ "link" \ "@href").text.split("/").last
  lazy val title = entry \ "title" text
  lazy val content = entry \ "content" text
  lazy val updated = entry \ "updated" text
  lazy val published = entry \ "published" text
  lazy val dateYYYYMMDD: String = published.take(PUBLISHED_TIME_FORMAT.length) filterNot isHighen

  def open() {
    val out = title + "\n" + content
    println(out)
  }
}

