package jp.ddo.ttoshi.hatena
import jp.ddo.ttoshi.wsse._

import java.io.File
import scala.xml.{XML, Elem}
import scala.io._
import org.apache.http._
import org.apache.http.client._
import org.apache.http.client.methods._
import org.apache.http.entity._
import org.apache.http.impl.client._
import org.apache.http.impl.conn._


class HatenaHttpClient(user: HatenaUser) {
  val API :HatenaAPI = new HatenaAPI(user)
  val httpClient :DefaultHttpClient = new DefaultHttpClient()
  val routePlanner = new ProxySelectorRoutePlanner(httpClient.getConnectionManager.getSchemeRegistry, ProxySelector.getDefault)
  httpClient.setRoutePlanner(routePlanner);
  lazy val wsseHeaderValue = (new WSSE(user)).header
  private def getEntity(response: HttpResponse) :Option[HttpEntity] = {
    val statusCode: Int = response.getStatusLine().getStatusCode()
    val entity: HttpEntity = response.getEntity
    statusCode match {
      case 200 => if (entity != null) Some(entity) else None
      case _   => None
    }
  }

  def entityToXML(entity: HttpEntity) :Elem = {
    val src = Source.fromInputStream(entity.getContent)(Codec("UTF-8"))
    val xml = XML.loadString(src.getLines.mkString("\n"))
    src.close()
    xml
  }

  def GET(url: URL): Elem =  {
    val request: HttpGet = new HttpGet(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    val response: HttpResponse = httpClient.execute(request)
    val entity: Option[HttpEntity] = getEntity(response)
    entity match {
      case None => error("Error")
      case Some(e) => {
        entityToXML(e)
      }
    }
  }

  def POST(url: URL, draftFile: DraftFile): Boolean = {
    println(draftFile.toXML.toString)
    POST(url, draftFile.toXML.toString)
  }

  private def POST(url: URL, entity: HttpEntity): Boolean = {
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

  def DELETE(url: URL): Boolean = {
    val request = new HttpDelete(url.is)
    println(url.is)
    request.addHeader("X-WSSE", wsseHeaderValue)
    val response: HttpResponse =  httpClient.execute(request)
    val statusCode: Int = response.getStatusLine.getStatusCode
    println(statusCode)
    statusCode / 100 match {
      case 2 => true
      case _ => false
    }
  }

}
