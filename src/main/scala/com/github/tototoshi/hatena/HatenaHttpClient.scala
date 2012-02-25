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

class HatenaHttpClient(user: HatenaUser) {
  val API :HatenaAPI = new HatenaAPI(user)
  val httpClient :DefaultHttpClient = new DefaultHttpClient()
  val routePlanner = new ProxySelectorRoutePlanner(httpClient.getConnectionManager.getSchemeRegistry, ProxySelector.getDefault)
  httpClient.setRoutePlanner(routePlanner);
  lazy val wsseHeaderValue = (new WSSE(user)).header
  private def getEntity(response: HttpResponse) :Option[HttpEntity] = {
    val statusCode: Int = response.getStatusLine().getStatusCode()
    val entity: HttpEntity = response.getEntity
    statusCode / 100 match {
      case 2 => if (entity != null) Some(entity) else None
      case _ => None
    }
  }

  private def entityToXML(entity: HttpEntity) :Elem = {
    val src = Source.fromInputStream(entity.getContent)(Codec("UTF-8"))
    val xml = XML.loadString(src.getLines.mkString("\n"))
    src.close()
    xml
  }

  def GET(url: URL): Elem =  {
    val request: HttpGet = new HttpGet(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    val response: HttpResponse = httpClient.execute(request)
    import scala.collection.JavaConversions._
    println(request.getFirstHeader("X-WSSE"))
    println(response)
    val entity: Option[HttpEntity] = getEntity(response)
    entity match {
      case None => sys.error("Error: Failed to get entiry. URL is %s".format(url))
      case Some(e) => {
        entityToXML(e)
      }
    }
  }

  def POST(url: URL, draftFile: DraftFile): Boolean = {
    POST(url, draftFile.toXML.toString)
  }

  def POST(url: URL, entity: HttpEntity): Boolean = {
    val request: HttpPost = new HttpPost(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    request.setEntity(entity)
    val response: HttpResponse = httpClient.execute(request)
    val statusCode: Int = response.getStatusLine.getStatusCode
    statusCode / 100 match {
      case 2 => true
      case _ => false
    }
  }

  def POST(url: URL, txt: String): Boolean = {
    val charset = "UTF-8"
    POST(url, new StringEntity(txt, charset))
  }

  def POST(url: URL, file: File): Boolean = {
    val contentType: String = "text/plain; charset=\"UTF-8\""
    val fileEntity: HttpEntity = new FileEntity(file, contentType)
    POST(url, fileEntity)
  }

  def PUT(url: URL, draftFile: DraftFile): Boolean = {
    POST(url, draftFile.toXML.toString)
  }

  def PUT(url: URL, entity: HttpEntity): Boolean = {
    val request: HttpPost = new HttpPost(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    request.setEntity(entity)
    val response: HttpResponse = httpClient.execute(request)
    val statusCode: Int = response.getStatusLine.getStatusCode
    statusCode / 100 match {
      case 2 => true
      case _ => false
    }
  }

  def PUT(url: URL, txt: String): Boolean = {
    val charset = "UTF-8"
    PUT(url, new StringEntity(txt, charset))
  }

  def PUT(url: URL, file: File): Boolean = {
    val contentType: String = "text/plain; charset=\"UTF-8\""
    val fileEntity: HttpEntity = new FileEntity(file, contentType)
    PUT(url, fileEntity)
  }

  def DELETE(url: URL): Boolean = {
    val request = new HttpDelete(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    val response: HttpResponse =  httpClient.execute(request)
    val statusCode: Int = response.getStatusLine.getStatusCode
    statusCode / 100 match {
      case 2 => true
      case _ => false
    }
  }

}
