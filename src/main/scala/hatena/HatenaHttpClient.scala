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

class HatenaHttpClient(user: HatenaUser) {
  val API :HatenaAPI = new HatenaAPI(user)
  val httpClient :HttpClient = new DefaultHttpClient()
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

  def GET(url: String): Elem =  {
    val request: HttpGet = new HttpGet(url)
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

  def POST(url: String, file: File): Boolean = {
    val request: HttpPost = new HttpPost(url)
    request.addHeader("X-WSSE", wsseHeaderValue)
    val contentType: String = "text/plain; charset=\"UTF-8\""
    val data: HttpEntity = new FileEntity(file, contentType)
    request.setEntity(data)
    val response: HttpResponse = httpClient.execute(request)
    val statusCode: Int = response.getStatusLine.getStatusCode
    statusCode / 100 match {
      case 2 => true
      case _ => false
    }
  }
}
