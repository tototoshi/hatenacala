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

import java.io.File
import java.net.ProxySelector
import org.apache.http._
import org.apache.http.client._
import org.apache.http.client.methods._
import org.apache.http.entity._
import org.apache.http.impl.client._
import org.apache.http.impl.conn._
import scala.io._
import scala.xml.{XML, Elem}

import com.github.tototoshi.wsse._

class HatenaHttpClient(user: HatenaUser) extends Using {
  val API: HatenaAPI = new HatenaAPI(user)

  val charset = "UTF-8"
  val contentType: String = "text/plain; charset=\"%s\"".format(charset)
  val httpClient :DefaultHttpClient = new DefaultHttpClient()
  val routePlanner = new ProxySelectorRoutePlanner(httpClient.getConnectionManager.getSchemeRegistry, ProxySelector.getDefault)

  httpClient.setRoutePlanner(routePlanner)

  lazy val wsseHeaderValue = (new WSSE(user)).header

  private def getEntity(response: HttpResponse) :Option[HttpEntity] =
    if (response.getStatusLine.getStatusCode / 100 == 2) Option(response.getEntity) else None

  private def entityToXML(entity: HttpEntity) :Elem =
    using(Source.fromInputStream(entity.getContent)(Codec("UTF-8"))) { src =>
      XML.loadString(src.getLines.mkString("\n"))
    }

  def GET(url: URL): Elem =  {
    val request: HttpGet = new HttpGet(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    getEntity(httpClient.execute(request)).map(entityToXML).getOrElse(sys.error("Error: Failed to get entiry. URL is %s".format(url)))
  }

  def POST(url: URL, draftFile: DraftFile): Boolean =
    POST(url, draftFile.toXML.toString)

  def POST(url: URL, entity: HttpEntity): Boolean = {
    val request: HttpPost = new HttpPost(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    request.setEntity(entity)
    httpClient.execute(request).getStatusLine.getStatusCode / 100 == 2
  }

  def POST(url: URL, txt: String): Boolean =
    POST(url, new StringEntity(txt, charset))

  def POST(url: URL, file: File): Boolean =
    POST(url, new FileEntity(file, contentType))

  def PUT(url: URL, draftFile: DraftFile): Boolean =
    POST(url, draftFile.toXML.toString)

  def PUT(url: URL, entity: HttpEntity): Boolean = {
    val request: HttpPost = new HttpPost(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    request.setEntity(entity)
    httpClient.execute(request).getStatusLine.getStatusCode / 100 == 2
  }

  def PUT(url: URL, txt: String): Boolean =
    PUT(url, new StringEntity(txt, charset))

  def PUT(url: URL, file: File): Boolean =
    PUT(url, new FileEntity(file, contentType))

  def DELETE(url: URL): Boolean = {
    val request = new HttpDelete(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    httpClient.execute(request).getStatusLine.getStatusCode / 100 == 2
  }

}
